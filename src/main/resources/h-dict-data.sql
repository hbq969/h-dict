insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('dict_source','字典数据来源',1,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('county','国家',2,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('enabled','启停状态',1,'key','value');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','1','固定值枚举');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','2','数据库表枚举');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','1','启用');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','0','停用');
insert into h_dict_sql(dict_name,sql_content) values('county','select c_key AS "key",c_name AS "value" from h_dict_sql_county');

insert into h_dict_sql_county(c_key,c_name) values('CN','中国');
insert into h_dict_sql_county(c_key,c_name) values('JP','日本');
insert into h_dict_sql_county(c_key,c_name) values('US','美国');