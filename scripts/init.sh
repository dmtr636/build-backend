sudo apt update
sudo apt upgrade

# Add swap file
sudo fallocate -l 1G /swap.img
sudo chmod 600 /swap.img
sudo mkswap /swap.img
sudo swapon /swap.img
sudo echo "/swap.img swap swap defaults 0 0" >> /etc/fstab
sudo swapon --show

# Vacuum size /var/log/journal
sudo journalctl --vacuum-size=100M
sudo echo "SystemMaxUse=100M" >> /etc/systemd/journald.conf
sudo systemctl restart systemd-journald

# fail2ban
sudo apt install fail2ban
sudo systemctl start fail2ban
sudo systemctl enable fail2ban

# .env
cp .env.template .env