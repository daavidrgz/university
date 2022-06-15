# 1.- SSH

## b) Secure copy

* Command:

	```console
	scp -c <cypher> lsi@<ip>:/path/to/file /dest/path
	# To see available cyphers
	ssh -Q cipher
	```

## c) Authorized and public keys

* Generate keys:

	```console
	ssh-keygen
	```

* Create `authorized-keys` files:

	```console
	touch ~/.ssh/authorized-keys
	chmod 600 ~/.ssh/authorized-keys
	```

* Copy id_rsa.pub content to authorized-keys file:

	```console
	cat id_rsa.pub > ~/.ssh/authorized-keys
	```

## d) SSH tunnel

* Command:

	```console
	ssh lsi@<ip> -N -f -L local_port:<ip>:remote_port
	```

* Options:
	* `-N`: No interactive
	* `-f`: Background execution
	* `-L`: Local port forwarding

## e) SSH file system

```
sudo apt install sshfs
sshfs lsi@<ip>:/path/to/share /mnt/sshfs 
```

# 6.- Stateful firewall
