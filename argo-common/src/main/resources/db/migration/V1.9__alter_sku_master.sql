alter table sku_master
	add vendor_id integer;

create index sku_master_index01
	on sku_master (vendor_id);