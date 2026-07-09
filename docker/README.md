# Docker - Setup local

Este guia centraliza a configuracao minima para rodar os servicos localmente usando PostgreSQL em Docker.

## Pre-requisitos

- Docker Engine instalado
- Java 21+
- Maven 3.9+

Validar ferramentas:

```bash
docker --version
java -version
mvn -version
```

## 1) Validar se o Docker daemon esta ativo

```bash
docker info > /dev/null && echo "Docker OK" || echo "Docker parado"
```

Se estiver parado (Linux com systemd):

```bash
sudo systemctl start docker
sudo systemctl enable docker
```

## 2) Subir PostgreSQL para o projeto

Nome padrao recomendado do container: `eop-postgres`.

Verificar se ja esta rodando:

```bash
docker ps --filter "name=eop-postgres" --filter "status=running"
```

Se o container ja existe e esta parado:

```bash
docker start eop-postgres
```

Se o container ainda nao existe, criar e iniciar:

```bash
docker run -d \
  --name eop-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=postgres \
  -p 5432:5432 \
  postgres:16
```

Checar status:

```bash
docker ps --filter "name=eop-postgres"
```

## 3) Criar bancos e usuarios dos servicos

Executar uma vez (idempotente para roles):

```bash
docker exec -i eop-postgres psql -U postgres -d postgres <<'SQL'
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'catalog') THEN
	  CREATE ROLE catalog LOGIN PASSWORD 'catalog';
   ELSE
	  ALTER ROLE catalog WITH LOGIN PASSWORD 'catalog';
   END IF;

   IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'inventory') THEN
	  CREATE ROLE inventory LOGIN PASSWORD 'inventory';
   ELSE
	  ALTER ROLE inventory WITH LOGIN PASSWORD 'inventory';
   END IF;

   IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'order') THEN
	  CREATE ROLE "order" LOGIN PASSWORD 'order';
   ELSE
	  ALTER ROLE "order" WITH LOGIN PASSWORD 'order';
   END IF;
END
$$;
SQL
```

Criar bancos se nao existirem:

```bash
docker exec -i eop-postgres psql -U postgres -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='catalog'" | grep -q 1 || \
docker exec -i eop-postgres psql -U postgres -d postgres -c "CREATE DATABASE catalog OWNER catalog;"

docker exec -i eop-postgres psql -U postgres -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='inventory'" | grep -q 1 || \
docker exec -i eop-postgres psql -U postgres -d postgres -c "CREATE DATABASE inventory OWNER inventory;"

docker exec -i eop-postgres psql -U postgres -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='order'" | grep -q 1 || \
docker exec -i eop-postgres psql -U postgres -d postgres -c "CREATE DATABASE \"order\" OWNER \"order\";"
```

Validar acessos:

```bash
docker exec -e PGPASSWORD=catalog -i eop-postgres psql -U catalog -d catalog -tAc 'SELECT current_user, current_database();'
docker exec -e PGPASSWORD=inventory -i eop-postgres psql -U inventory -d inventory -tAc 'SELECT current_user, current_database();'
docker exec -e PGPASSWORD=order -i eop-postgres psql -U order -d order -tAc 'SELECT current_user, current_database();'
```

## 4) Rodar os servicos

Defaults dos servicos:

- `catalog-service`: `jdbc:postgresql://localhost:5432/catalog`, user `catalog`, senha `catalog`
- `inventory-service`: `jdbc:postgresql://localhost:5432/inventory`, user `inventory`, senha `inventory`
- `order-service`: `jdbc:postgresql://localhost:5432/order`, user `order`, senha `order`

Subir localmente:

```bash
cd services/catalog-service
mvn spring-boot:run
```

```bash
cd services/inventory-service
mvn spring-boot:run
```

```bash
cd services/order-service
mvn spring-boot:run
```

## 5) Override de configuracao (opcional)

Se quiser usar outras credenciais/porta, exporte variaveis antes de subir cada servico:

```bash
export CATALOG_DB_URL=jdbc:postgresql://localhost:5432/catalog
export CATALOG_DB_USERNAME=catalog
export CATALOG_DB_PASSWORD=catalog

export INVENTORY_DB_URL=jdbc:postgresql://localhost:5432/inventory
export INVENTORY_DB_USERNAME=inventory
export INVENTORY_DB_PASSWORD=inventory

export ORDER_DB_URL=jdbc:postgresql://localhost:5432/order
export ORDER_DB_USERNAME=order
export ORDER_DB_PASSWORD=order
```

## Troubleshooting rapido

- Erro `password authentication failed for user "inventory"`:
  - Reaplique o passo 3 para recriar/atualizar role e senha.
- Erro `password authentication failed for user "order"`:
  - Reaplique o passo 3 para recriar/atualizar role e senha do `order`.
- Container em uso com outro nome:
  - Liste com `docker ps -a` e ajuste os comandos para o nome existente.
- Porta `5432` ocupada:
  - Suba em outra porta (`-p 5433:5432`) e ajuste `CATALOG_DB_URL` / `INVENTORY_DB_URL` / `ORDER_DB_URL`.

