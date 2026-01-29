#!/bin/bash
set -e

docker exec -i shard0 psql -U user -d myDb0 <<EOF
CREATE USER replicator WITH REPLICATION PASSWORD 'replicapass';
\du
EOF

docker exec -i shard1 psql -U user -d myDb1 <<EOF
CREATE USER replicator WITH REPLICATION PASSWORD 'replicapass';
\du
EOF


docker exec shard0 bash -c "echo 'host replication replicator all md5' >> /var/lib/postgresql/data/pg_hba.conf"
docker exec shard1 bash -c "echo 'host replication replicator all md5' >> /var/lib/postgresql/data/pg_hba.conf"

docker exec shard0 psql -U user -d myDb0 -c "SELECT pg_reload_conf();"
docker exec shard1 psql -U user -d myDb1 -c "SELECT pg_reload_conf();"

docker stop shard0-replica shard1-replica

docker start shard0-replica shard1-replica

sleep 5

docker exec shard0-replica bash -c "rm -rf /var/lib/postgresql/data/*"
docker exec shard0-replica bash -c "PGPASSWORD=replicapass pg_basebackup -h shard0 -U replicator -D /var/lib/postgresql/data -Fp -Xs -R"

docker exec shard1-replica bash -c "rm -rf /var/lib/postgresql/data/*"
docker exec shard1-replica bash -c "PGPASSWORD=replicapass pg_basebackup -h shard1 -U replicator -D /var/lib/postgresql/data -Fp -Xs -R"

docker exec shard0-replica touch /var/lib/postgresql/data/standby.signal
docker exec shard1-replica touch /var/lib/postgresql/data/standby.signal

docker exec -i shard0-replica bash -c "echo \"primary_conninfo='host=shard0 port=5432 user=replicator password=replicapass'\" >> /var/lib/postgresql/data/postgresql.auto.conf"
docker exec -i shard1-replica bash -c "echo \"primary_conninfo='host=shard1 port=5432 user=replicator password=replicapass'\" >> /var/lib/postgresql/data/postgresql.auto.conf"

docker restart shard0-replica shard1-replica

docker exec shard0 psql -U user -d myDb0 -c "SELECT * FROM pg_stat_replication"
docker exec shard1 psql -U user -d myDb1 -c "SELECT * FROM pg_stat_replication"


