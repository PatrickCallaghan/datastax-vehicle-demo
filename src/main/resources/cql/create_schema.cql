create keyspace if not exists datastax WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax;  
 
create table if not exists current_location (
	vehicle text PRIMARY KEY,
	tile1 text,
	tile2 text,	
	lat_long 'PointType',
	date timestamp,
	speed double,
	temperature double,
	p_ map<text,double>
) WITH compaction = { 'class' :  'LeveledCompactionStrategy'  };

CREATE SEARCH INDEX ON current_location with columns date, speed, p_, temperature
    AND OPTIONS {reindex: false, lenient: true};

ALTER SEARCH INDEX SCHEMA ON current_location ADD types.fieldtype[@class='solr.StrField', @name='string'];
ALTER SEARCH INDEX SCHEMA ON current_location ADD types.fieldtype[@class='solr.TextField', @name='text']
WITH '{"analyzer":{"tokenizer":{"class":"solr.StandardTokenizerFactory"}}}';
ALTER SEARCH INDEX SCHEMA ON current_location ADD types.fieldtype[@class='solr.SpatialRecursivePrefixTreeFieldType',
      @name='point', @distanceUnits='kilometers',
      @spatialContextFactory='org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory'];

ALTER SEARCH INDEX SCHEMA ON current_location ADD fields.field[@name='lat_long', @type='point',
      @indexed='true', @stored='true'];
ALTER SEARCH INDEX SCHEMA ON current_location SET fields.field[@name='vehicle']@type='string';
ALTER SEARCH INDEX SCHEMA ON current_location SET fields.field[@name='vehicle']@docValues='true';
ALTER SEARCH INDEX SCHEMA ON current_location ADD fields.field[@name='tile1', @type='text', @indexed='true', @docValues='true'];
ALTER SEARCH INDEX SCHEMA ON current_location ADD fields.field[@name='tile2', @type='text', @indexed='true', @docValues='true'];
RELOAD SEARCH INDEX ON current_location;

create table if not exists vehicle(
	vehicle text,
	day text,
	date timestamp, 
	lat_long 'PointType',
	tile text,
	speed double,
	temperature double,
        p_ map<text,double>,
	PRIMARY KEY ((vehicle,day), date)
) with clustering order by (date desc);

CREATE SEARCH INDEX ON vehicle with columns date, speed, temperature, p_
    AND OPTIONS {reindex: false};

ALTER SEARCH INDEX SCHEMA ON vehicle ADD types.fieldtype[@class='solr.StrField', @name='string'];
ALTER SEARCH INDEX SCHEMA ON vehicle ADD types.fieldtype[@class='solr.TextField', @name='text']
WITH '{"analyzer":{"tokenizer":{"class":"solr.StandardTokenizerFactory"}}}';
ALTER SEARCH INDEX SCHEMA ON vehicle ADD types.fieldtype[@class='solr.SpatialRecursivePrefixTreeFieldType',
      @name='point', @distanceUnits='kilometers',
      @spatialContextFactory='org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory'];

ALTER SEARCH INDEX SCHEMA ON vehicle ADD fields.field[@name='lat_long', @type='point', @indexed='true',
     @stored='true'];
ALTER SEARCH INDEX SCHEMA ON vehicle SET fields.field[@name='vehicle']@type='string';
ALTER SEARCH INDEX SCHEMA ON vehicle SET fields.field[@name='vehicle']@docValues='true';
ALTER SEARCH INDEX SCHEMA ON vehicle ADD fields.field[@name='tile', @type='text', @indexed='true', @docValues='true'];
ALTER SEARCH INDEX SCHEMA ON vehicle SET fields.field[@name='day']@type='string';
ALTER SEARCH INDEX SCHEMA ON vehicle SET fields.field[@name='day']@docValues='true';
RELOAD SEARCH INDEX ON vehicle;


create table if not exists vehicle_status (
    vehicle text,
    day text,
    state_change_time timestamp,
    vehicle_state text,
    PRIMARY KEY ((vehicle, day), state_change_time)
) with clustering order by (state_change_time desc);


truncate current_location;
truncate vehicle;
truncate vehicle_status;

