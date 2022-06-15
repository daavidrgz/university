#!/bin/bash

# IPs
myIp="10.11.48.155"
myIpInterna="10.11.50.155"
myIpVpn="10.8.0.2"
myIp6to4="2002:a0b:309b::1"

compaIp="10.11.48.154,10.11.48.153"
compaIpInterna="10.11.50.154,10.11.50.153"
compaIpVpn="10.8.0.1"
compaIp6to4="2002:a0b:309a::1,2002:a0b:3099::1"

# NTP
ntpServerIp="10.11.48.155"
# Rsyslog server
rsyslogServerIp="10.8.0.1"

# Networks
networks="10.20.0.0/16,10.30.0.0/16,10.31.0.0/16,10.32.0.0/16"
# DNS Servers
dns="10.8.12.47,10.8.12.50,10.8.12.49"
# Repositories
repos="151.101.242.132,23.223.71.193"

# CTRL+C
trap ctrl_c INT
function ctrl_c() {
	echo -e "\n-------------------------\n[*] Flushing tables..."
	iptables -F
	iptables -X
	iptables -P OUTPUT ACCEPT
	iptables -P INPUT ACCEPT
	iptables -P FORWARD ACCEPT

	ip6tables -F
	ip6tables -X
	ip6tables -P OUTPUT ACCEPT
	ip6tables -P INPUT ACCEPT
	ip6tables -P FORWARD ACCEPT
	exit 1;
}

if [ ! $(id -u) -eq 0 ]; then
	echo -e "\n[*] Must be run as root!\n"
	exit 1
fi

#####################
# Ipv4 #
#####################

iptables -F
iptables -X

# Default policies
iptables -P OUTPUT DROP
iptables -P INPUT DROP
iptables -P FORWARD DROP
iptables -t nat -F

# Accept all established or realated connections
iptables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT

# Accept all internal connections
iptables -A INPUT -m conntrack --ctstate NEW -i lo -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o lo -j ACCEPT

# SSH connections
iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p TCP --dport 22 -s $compaIp -d $myIp -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p TCP --dport 22 -s $myIp -d $compaIp -j ACCEPT

iptables -A INPUT -m conntrack --ctstate NEW -i ens34 -p TCP --dport 22 -s $compaIpInterna -d $myIpInterna -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens34 -p TCP --dport 22 -s $myIpInterna -d $compaIpInterna -j ACCEPT

iptables -A INPUT -m conntrack --ctstate NEW -i vpn0 -p TCP --dport 22 -s $compaIpVpn -d $myIpVpn -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o vpn0 -p TCP --dport 22 -s $myIpVpn -d $compaIpVpn -j ACCEPT

iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p TCP --dport 22 -s $networks -d $myIp -j ACCEPT

# NTP connections
if [ "$ntpServerIp" == "$myIp" ]; then
	iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p UDP -s $compaIp -d $myIp --dport 123 -j ACCEPT
else
	iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p UDP -s $myIp -d $ntpServerIp --dport 123 -j ACCEPT
fi

# Rsyslog connections
if [ "$rsyslogServerIp" == "$myIpVpn" ]; then
	iptables -A INPUT -m conntrack --ctstate NEW -i vpn0 -p TCP -s $compaIpVpn -d $rsyslogServerIp --dport 514 -j ACCEPT
else
	iptables -A OUTPUT -m conntrack --ctstate NEW -o vpn0 -p TCP -s $myIpVpn -d $rsyslogServerIp --dport 514 -j ACCEPT
fi

# Apache connections
iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p TCP -s $compaIp -d $myIp -m multiport --dports 80,443 -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p TCP -s $myIp -d $compaIp -m multiport --dports 80,443 -j ACCEPT

iptables -A INPUT -m conntrack --ctstate NEW -i ens34 -p TCP -s $compaIpInterna -d $myIpInterna -m multiport --dports 80,443 -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens34 -p TCP -s $myIpInterna -d $compaIpInterna -m multiport --dports 80,443 -j ACCEPT

iptables -A INPUT -m conntrack --ctstate NEW -i vpn0 -p TCP -s $compaIpVpn -d $myIpVpn -m multiport --dports 80,443 -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o vpn0 -p TCP -s $myIpVpn -d $compaIpVpn -m multiport --dports 80,443 -j ACCEPT

# Vpn connections
iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p UDP -s $compaIp -d $myIp --dport 1194 --sport 1194 -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p UDP -s $myIp -d $compaIp --dport 1194 --sport 1194 -j ACCEPT

# ICMP
iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p ICMP -s $compaIp -d $myIp -j ACCEPT
iptables -A INPUT -m conntrack --ctstate NEW -i ens34 -p ICMP -s $compaIpInterna -d $myIpInterna -j ACCEPT
iptables -A INPUT -m conntrack --ctstate NEW -i vpn0 -p ICMP -s $compaIpVpn -d $myIpVpn -j ACCEPT

iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p ICMP -s $myIp -d $compaIp -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens34 -p ICMP -s $myIpInterna -d $compaIpInterna -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o vpn0 -p ICMP -s $myIpVpn -d $compaIpVpn -j ACCEPT

# 6to4 tunnel
iptables -A INPUT -m conntrack --ctstate NEW -i ens33 -p IPv6 -s $compaIp -d $myIp -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p IPv6 -s $myIp -d $compaIp -j ACCEPT

# Repos
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p TCP -s $myIp -d $repos --dport 80 -j ACCEPT

# DNS
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p UDP -s $myIp -d $dns --dport 53 -j ACCEPT
iptables -A OUTPUT -m conntrack --ctstate NEW -o ens33 -p TCP -s $myIp -d $dns --dport 53 -j ACCEPT

# Logging
iptables -A INPUT -p TCP --dport 22 -j LOG --log-level 4 --log-prefix "[ssh rejected]: " 
iptables -A INPUT -p ICMP -j LOG --log-level 4 --log-prefix "[icmp rejected]: "
iptables -A INPUT -p TCP ! --dport 22 -j LOG --log-level 4 --log-prefix "[tcp connection rejected]: "

# Reject all incoming connections
iptables -A INPUT -p TCP -j REJECT --reject-with tcp-reset
iptables -A INPUT -p UDP -j REJECT --reject-with icmp-port-unreachable

#####################
# Ipv6 #
#####################

ip6tables -F
ip6tables -X

# Default policies
ip6tables -P OUTPUT DROP
ip6tables -P INPUT DROP
ip6tables -P FORWARD DROP
ip6tables -t nat -F

# Accept all established or realated connections
ip6tables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT
ip6tables -A OUTPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT

# Accept all internal connections
ip6tables -A INPUT -m conntrack --ctstate NEW -i lo -j ACCEPT
ip6tables -A OUTPUT -m conntrack --ctstate NEW -o lo -j ACCEPT

# SSH 6to4 connections
ip6tables -A INPUT -m conntrack --ctstate NEW -i 6to4 -p TCP -s $compaIp6to4 -d $myIp6to4 --dport 22 -j ACCEPT
ip6tables -A OUTPUT -m conntrack --ctstate NEW -o 6to4 -p TCP -s $myIp6to4 -d $compaIp6to4 --dport 22 -j ACCEPT

# ICMP 6to4
ip6tables -A INPUT -m conntrack --ctstate NEW -i 6to4 -p ICMPv6 -s $compaIp6to4 -d $myIp6to4 -j ACCEPT
ip6tables -A OUTPUT -m conntrack --ctstate NEW -o 6to4 -p ICMPv6 -s $myIp6to4 -d $compaIp6to4 -j ACCEPT
# ICMP link-local
ip6tables -A FORWARD -m conntrack --ctstate NEW -i ens33 -p ICMPv6 -s $compaIpv6 -d $myIpv6 -j ACCEPT

# Logging
ip6tables -A INPUT -p TCP --dport 22 -j LOG --log-level 4 --log-prefix "[ssh ipv6 rejected]: " 
ip6tables -A INPUT -p ICMPv6 -j LOG --log-level 4 --log-prefix "[icmpv6 rejected]: "
ip6tables -A INPUT -p TCP ! --dport 22 -j LOG --log-level 4 --log-prefix "[tcp ipv6 connection rejected]: "

# Reject all incoming connections
ip6tables -A INPUT -p TCP -j REJECT --reject-with tcp-reset
ip6tables -A INPUT -p UDP -j REJECT --reject-with icmp-port-unreachable

echo -e "[*] Final tables:"
iptables -L

sleep 120

ctrl_c
