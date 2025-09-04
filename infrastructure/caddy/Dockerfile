FROM caddy:2.8.4-builder-alpine AS builder

RUN xcaddy build --with github.com/dmtr636/caddy-dns-timeweb@v1.0.3

FROM caddy:2.8.4-alpine

COPY --from=builder /usr/bin/caddy /usr/bin/caddy