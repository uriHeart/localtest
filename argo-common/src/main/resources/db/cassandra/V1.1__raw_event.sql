CREATE TABLE argo.raw_event (
  vendor_id bigint,
  channel_id bigint,
  order_id varchar,
  data text,
  format varchar,
  auto boolean,
  published_at timestamp,
  created_at timestamp,
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