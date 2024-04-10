#/usr/bin/env bash

set -e

# Health check
pg_isready -d ${POSTGRES_DB} -U ${POSTGRES_USER}

# On success
if [ ! -f /tmp/.FIRST_READY_STATUS_FLAG ]; then

  # On first success set the FLAG and populate the database
  touch /tmp/.FIRST_READY_STATUS_FLAG
  psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} -a -f /init_db.sql
fi