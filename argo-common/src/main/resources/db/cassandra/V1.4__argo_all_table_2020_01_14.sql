create table argo_order
(
	vendor_id bigint,
	channel_id bigint,
	order_id text,
	published_at timestamp,
	created_at timestamp,
	event text,
	metadata text,
	paid_at timestamp,
	parent_order_id text,
	payment_type text,
	replay_count int,
	state text,
	total_amount bigint,
	tracking_log text,
	primary key ((vendor_id, channel_id, order_id), published_at)
)
with clustering order by (published_at desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table argo_refine_address
(
	original_address_hash text,
	refine_date timestamp,
	jibun_address text,
	latitude double,
	longitude double,
	original_address text,
	road_address text,
	primary key (original_address_hash, refine_date)
)
with clustering order by (refine_date desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table argo_sku_lifecycle
(
	sku_id uuid,
	published_at timestamp,
	channel_id bigint,
	created_at timestamp,
	event text,
	metadata text,
	order_id text,
	replay_count int,
	state text,
	tracking_log text,
	vendor_id bigint,
	vendor_item_id uuid,
	primary key (sku_id, published_at)
)
with clustering order by (published_at desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table conversion_template
(
	source_id text,
	target_id text,
	created_at timestamp,
	expired_at timestamp,
	conversion_rules frozen<list<frozen<conversion_rule>>>,
	primary key (source_id, target_id, created_at, expired_at)
)
with clustering order by (target_id desc, created_at desc, expired_at asc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table external_vendor_item_mapping
(
	vendor_id bigint,
	source_item_id text,
	source_item_name text,
	source_item_option text,
	vendor_item_id text,
	created_at timestamp,
	primary key ((vendor_id, source_item_id, source_item_name, source_item_option), vendor_item_id)
)
with clustering order by (vendor_item_id desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table order_address
(
	vendor_id bigint,
	channel_id bigint,
	order_id text,
	published_at timestamp,
	created_at timestamp,
	delivery_request text,
	orderer text,
	original_address text,
	original_postalcode text,
	recipient text,
	refined_address text,
	refined_postalcode text,
	primary key ((vendor_id, channel_id, order_id), published_at)
)
with clustering order by (published_at desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table order_vendor_item_lifecycle
(
	vendor_id bigint,
	channel_id bigint,
	order_id text,
	vendor_item_id text,
	published_at timestamp,
	created_at timestamp,
	event text,
	metadata text,
	quantity int,
	replay_count int,
	sku_mappings list<bigint>,
	source_item_id text,
	source_item_name text,
	source_item_option text,
	state text,
	primary key ((vendor_id, channel_id, order_id), vendor_item_id, published_at)
)
with clustering order by (vendor_item_id desc, published_at desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

create table raw_event
(
	vendor_id bigint,
	channel_id bigint,
	order_id text,
	published_at timestamp,
	auto boolean,
	created_at timestamp,
	data text,
	event text,
	format text,
	primary key ((vendor_id, channel_id, order_id), published_at)
)
with clustering order by (published_at desc)
	and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
	and compaction = {'max_threshold': '32', 'min_threshold': '4', 'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy'}
	and compression = {'class': 'org.apache.cassandra.io.compress.LZ4Compressor', 'chunk_length_in_kb': '16'}
	and dclocal_read_repair_chance = 0.0;

