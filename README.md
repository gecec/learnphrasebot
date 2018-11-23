# learnphrasebot

On Linux machine you should be a super user 
Build:
$ docker build -t java-app:learnphrasebot .

Run:
$ docker run java-app:learnphrasebot


Offtop
Idea Hints for Docker plugin:
 
I assume, your username is already in docker group. To check this, issue below command.

id -nG
If not you need to add your user into the docker group by below command.

sudo groupadd docker
sudo usermod -aG docker $USER

When you execute the command, `sudo systemctl start docker`, it creates a docker process. That docker process contains `dockerd` daemon thread. The command also creates default `docker.sock` Unix socket. The `docker.sock` socket is continuously listened by `dockerd` daemon thread. This makes you can do kernel-level IPC with `docker.pid` process. To be able to use this docker socket, you need to have proper permission from the process level (`docker.pid`) and file level (`docker.sock`). So, executing below two commands should solve your issue.

sudo chmod a+rwx /var/run/docker.sock
sudo chmod a+rwx /var/run/docker.pid

As you see, it doesn't show any error in PyCharm. 