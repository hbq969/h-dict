delete from h_dict_base where dict_name in ('dict_source','county','enabled');
delete from h_dict_pairs where dict_name in ('dict_source','county','enabled');
truncate table h_dict_sql_county;

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('dict_source','辞書データソース',1,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('county','国家',2,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('enabled','開始/停止ステータス',1,'key','value');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','1','固定値の列挙');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','2','データベーステーブルの列挙');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','1','有効にする');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','0','無効化');
insert into h_dict_sql(dict_name,sql_content) values('county','select c_key AS "key",c_name AS "value" from h_dict_sql_county');

insert into h_dict_sql_county(c_key,c_name) values('CN','中国');
insert into h_dict_sql_county(c_key,c_name) values('JP','日本');
insert into h_dict_sql_county(c_key,c_name) values('US','アメリカ合衆国');