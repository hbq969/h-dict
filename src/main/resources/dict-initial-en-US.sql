delete from h_dict_base where dict_name in ('dict_source','county','enabled');
delete from h_dict_pairs where dict_name in ('dict_source','county','enabled');
truncate table h_dict_sql_county;

insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('dict_source','Dictionary DataSource',1,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('county','County',2,'key','value');
insert into h_dict_base(dict_name,dict_desc,dict_source,key_column,val_column) values('enabled','Enabled',1,'key','value');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','1','Fixed Value Enumeration');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('dict_source','2','Database Table Enumeration');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','1','enabled');
insert into h_dict_pairs(dict_name,pair_key,pair_value) values('enabled','0','disabled');
insert into h_dict_sql(dict_name,sql_content) values('county','select c_key AS "key",c_name AS "value" from h_dict_sql_county');

insert into h_dict_sql_county(c_key,c_name) values('CN','China');
insert into h_dict_sql_county(c_key,c_name) values('JP','Japan');
insert into h_dict_sql_county(c_key,c_name) values('US','United States');