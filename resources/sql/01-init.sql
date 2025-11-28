-- Schema migrations tracking
CREATE TABLE IF NOT EXISTS schema_migrations (
  version text PRIMARY KEY,
  executed_at timestamptz NOT NULL DEFAULT now()
);

-- Main resource table
CREATE TABLE IF NOT EXISTS resource (
  id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  resource_type text NOT NULL,
  logical_id    text NOT NULL,
  version_id    bigint NOT NULL DEFAULT 1,
  last_updated  timestamptz NOT NULL DEFAULT now(),
  txid          bigint NOT NULL DEFAULT txid_current(),
  status        text NOT NULL DEFAULT 'active',
  data          jsonb NOT NULL,
  UNIQUE (resource_type, logical_id, version_id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_resource_type ON resource(resource_type);
CREATE INDEX IF NOT EXISTS idx_resource_data_gin ON resource USING gin (data jsonb_path_ops);
CREATE INDEX IF NOT EXISTS idx_resource_logical_id ON resource(resource_type, logical_id) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_resource_last_updated ON resource(last_updated);

