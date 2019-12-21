# 创建容器
docker run --name cpto-dapp-nginx -p 80:80 -p 443:443 -d --restart=always \
    -v /data/cpto-dapp-prod/docker/nginx/html:/usr/share/nginx/html \
    -v /data/cpto-dapp-prod/docker/nginx/conf:/etc/nginx \
    -v /data/cpto-dapp-prod/docker/nginx/logs:/var/log/nginx \
    -v /data/cpto-dapp-prod/docker/nginx/ssl:/etc/ssl/certs \
    swr.cn-north-1.myhuaweicloud.com/chaincat-base/nginx