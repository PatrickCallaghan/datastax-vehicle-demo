create keyspace if not exists datastax WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax;  

create table if not exists vehicle_current_reading (
	vehicle_id text PRIMARY KEY,
	geohash frozen<list<text>>,
	lat_long 'PointType',
	date timestamp,
	speed double,
	temperature double,
	p_ map<text,double>
) WITH compaction = { 'class' :  'LeveledCompactionStrategy'  };

CREATE SEARCH INDEX ON vehicle_current_reading WITH COLUMNS date, speed, p_, temperature AND OPTIONS {reindex: false, lenient: true};

ALTER SEARCH INDEX SCHEMA ON vehicle_current_reading ADD types.fieldtype[@class='solr.SpatialRecursivePrefixTreeFieldType',
      @name='rpt', @distanceUnits='kilometers', @geo='true',
      @spatialContextFactory='org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory'];

ALTER SEARCH INDEX SCHEMA ON vehicle_current_reading SET fields.field[@name='vehicle_id'] @docValues='true';
ALTER SEARCH INDEX SCHEMA ON vehicle_current_reading ADD fields.field[@name='lat_long', @type='rpt', @indexed='true', @stored='true', @multiValued='false'];
ALTER SEARCH INDEX SCHEMA ON vehicle_current_reading ADD fields.field[@name='geohash', @type='StrField', @indexed='true', @stored='true', @multiValued='true', @docValues='true'];
RELOAD SEARCH INDEX ON vehicle_current_reading;


create table if not exists vehicle_historical_readings (
	vehicle_id text,
	day text,
	date timestamp, 
	lat_long 'PointType',
	geohash frozen<list<text>>,
	speed double,
	temperature double,
    p_ map<text,double>,
	PRIMARY KEY ((vehicle_id,day), date)
) with clustering order by (date desc);

CREATE SEARCH INDEX ON vehicle_historical_readings WITH COLUMNS speed, p_, temperature AND OPTIONS {reindex: false, lenient: true};

ALTER SEARCH INDEX SCHEMA ON vehicle_historical_readings ADD types.fieldtype[@class='solr.SpatialRecursivePrefixTreeFieldType', @name='rpt', @distanceUnits='kilometers',
                                                            @geo='true', @spatialContextFactory='org.locationtech.spatial4j.context.jts.JtsSpatialContextFactory'];

ALTER SEARCH INDEX SCHEMA ON vehicle_historical_readings SET fields.field[@name='vehicle_id'] @docValues='true';
ALTER SEARCH INDEX SCHEMA ON vehicle_historical_readings ADD fields.field[@name='lat_long', @type='rpt', @indexed='true', @stored='true', @multiValued='false'];
ALTER SEARCH INDEX SCHEMA ON vehicle_historical_readings ADD fields.field[@name='geohash', @type='StrField', @indexed='true', @stored='true', @multiValued='true', @docValues='true'];
RELOAD SEARCH INDEX ON vehicle_historical_readings;


create table if not exists vehicle_status (
    vehicle_id text,
    day text,
    state_change_time timestamp,
    vehicle_state text,
    PRIMARY KEY ((vehicle_id, day), state_change_time)
) with clustering order by (state_change_time desc);

truncate vehicle_current_reading;
truncate vehicle_historical_readings;
truncate vehicle_status;

