CREATE TABLE argo.argo_order (
  vendor_id bigint,
  channel_id bigint,
  order_id text,
  replay_count int,
  metadata text,
  state text,
  event text,
  tracking_log text,
  created_at timestamp,
  paid_at timestamp,
  published_at timestamp,
  PRIMARY KEY ((vendor_id, channel_id, order_id), published_at)
) WITH CLUSTERING ORDER BY (published_at DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';

CREATE TABLE argo.order_address (
  vendor_id bigint,
  channel_id bigint,
  order_id text,
  published_at timestamp,
  original_address text,
  oroginal_postalcode text,
  refined_address text,
  refined_postalcode text,
  recipient text,
  orderer text,
  delivery_request text,
  event text,
  created_at timestamp,
  published_at timestamp,
  PRIMARY KEY ((vendor_id, channel_id, order_id), published_at)
) WITH CLUSTERING ORDER BY (published_at DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';

CREATE TABLE argo.external_vendor_item_mapping (
  vendor_id bigint,
  source_item_id text,
  source_item_name text,
  source_item_option text,
  vendor_item_id text,
  created_at timestamp,
  PRIMARY KEY ((vendor_id, source_item_id, source_item_name, source_item_option), vendor_item_id)
) WITH CLUSTERING ORDER BY (vendor_item_id DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';

CREATE TABLE argo.order_vendor_item_lifecycle (
  vendor_id bigint,
  channel_id bigint,
  order_id text,
  vendor_item_id text,
  source_item_id text,
  source_item_name text,
  source_item_option text,
  metadata text,
  replay_count int,
  quantity int,
  event text,
  state text,
  sku_mappings text,
  created_at timestamp,
  published_at timestamp,
  PRIMARY KEY ((vendor_id, channel_id, order_id), vendor_item_id, published_at)
) WITH CLUSTERING ORDER BY (vendor_item_id desc, published_at DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';

CREATE TABLE argo.argo_sku_mapping (
  vendor_id bigint,
  channel_id bigint,
  order_id text,
  vendor_item_id uuid,
  sku_id uuid,
  created_at timestamp,
  PRIMARY KEY ((vendor_id, channel_id, order_id, vendor_item_id), sku_id)
) WITH CLUSTERING ORDER BY (sku_id DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';


CREATE TABLE argo.argo_sku (
  sku_id uuid,
  vendor_id bigint,
  channel_id bigint,
  order_id text,
  vendor_item_id uuid,
  replay_count int,
  quantity int,
  metadata text,
  state text,
  event text,
  tracking_log text,
  created_at timestamp,
  published_at timestamp,
  PRIMARY KEY ((sku_id), published_at)
) WITH CLUSTERING ORDER BY (published_at DESC)
 AND bloom_filter_fp_chance = 0.01
 AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
 AND comment = ''
 AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
 AND compression = {'chunk_length_in_kb': '16', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
 AND crc_check_chance = 1.0
 AND dclocal_read_repair_chance = 0.0
 AND default_time_to_live = 0
 AND gc_grace_seconds = 864000
 AND max_index_interval = 2048
 AND memtable_flush_period_in_ms = 0
 AND min_index_interval = 128
 AND read_repair_chance = 0.0
 AND speculative_retry = '99PERCENTILE';