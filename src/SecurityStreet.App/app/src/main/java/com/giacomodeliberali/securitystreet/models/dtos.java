/* Options:
Date: 2018-01-23 23:47:07
Version: 5.00
Tip: To override a DTO option, remove "//" prefix before updating
BaseUrl: http://unive-development-swe-2018.azurewebsites.net

Package: com.giacomodeliberali.securitystreet.Models
GlobalNamespace: dtos
//AddPropertyAccessors: True
//SettersReturnThis: True
//AddServiceStackTypes: True
//AddResponseStatus: False
//AddDescriptionAsComments: True
//AddImplicitVersion: 
//IncludeTypes: 
//ExcludeTypes: 
//TreatTypesAsStrings: 
//DefaultImports: java.math.*,java.util.*,net.servicestack.client.*,com.google.gson.annotations.*,com.google.gson.reflect.*
*/

package com.giacomodeliberali.securitystreet.models;

import java.math.*;
import java.util.*;
import net.servicestack.client.*;
import com.google.gson.annotations.*;
import com.google.gson.reflect.*;

public class dtos
{

    @Route(Path="/crashes/{Id}", Verbs="GET")
    // @Route(Path="/crashes", Verbs="GET")
    public static class ReadCrashRequest extends ReadRequest implements IReturn<ArrayList<CrashDto>>
    {
        
        private static Object responseType = new TypeToken<ArrayList<CrashDto>>(){}.getType();
        public Object getResponseType() { return responseType; }
    }

    @Route(Path="/autovelox/readByDistance/{Latitude}/{Longitude}/{Distance}", Verbs="GET")
    // @Route(Path="/autovelox/readByDistance/{Latitude}/{Longitude}", Verbs="GET")
    public static class ReadAutoveloxByDistanceRequest implements IReturn<ArrayList<AutoveloxDto>>
    {
        public Double latitude = null;
        public Double longitude = null;
        public Integer distance = null;
        
        public Double getLatitude() { return latitude; }
        public ReadAutoveloxByDistanceRequest setLatitude(Double value) { this.latitude = value; return this; }
        public Double getLongitude() { return longitude; }
        public ReadAutoveloxByDistanceRequest setLongitude(Double value) { this.longitude = value; return this; }
        public Integer getDistance() { return distance; }
        public ReadAutoveloxByDistanceRequest setDistance(Integer value) { this.distance = value; return this; }
        private static Object responseType = new TypeToken<ArrayList<AutoveloxDto>>(){}.getType();
        public Object getResponseType() { return responseType; }
    }

    @Route(Path="/autovelox/{Id}", Verbs="GET")
    // @Route(Path="/autovelox", Verbs="GET")
    public static class ReadAutoveloxRequest extends ReadRequest implements IReturn<ArrayList<AutoveloxDto>>
    {
        
        private static Object responseType = new TypeToken<ArrayList<AutoveloxDto>>(){}.getType();
        public Object getResponseType() { return responseType; }
    }

    @Route(Path="/query/crashes", Verbs="GET")
    // @Route(Path="/query/crashes", Verbs="POST")
    public static class QueryCrashRequest extends QueryDb<Crash> implements IReturn<QueryResponse<Crash>>
    {
        
        private static Object responseType = new TypeToken<QueryResponse<Crash>>(){}.getType();
        public Object getResponseType() { return responseType; }
    }

    @Route(Path="/query/autovelox", Verbs="GET")
    // @Route(Path="/query/autovelox", Verbs="POST")
    public static class QueryAutoveloxRequest extends QueryDb<Autovelox> implements IReturn<QueryResponse<Autovelox>>
    {
        
        private static Object responseType = new TypeToken<QueryResponse<Autovelox>>(){}.getType();
        public Object getResponseType() { return responseType; }
    }

    @DataContract
    public static class QueryResponse<T>
    {
        @DataMember(Order=1)
        public Integer offset = null;

        @DataMember(Order=2)
        public Integer total = null;

        @DataMember(Order=3)
        public ArrayList<T> results = null;

        @DataMember(Order=4)
        public HashMap<String,String> meta = null;

        @DataMember(Order=5)
        public ResponseStatus responseStatus = null;
        
        public Integer getOffset() { return offset; }
        public QueryResponse<T> setOffset(Integer value) { this.offset = value; return this; }
        public Integer getTotal() { return total; }
        public QueryResponse<T> setTotal(Integer value) { this.total = value; return this; }
        public ArrayList<T> getResults() { return results; }
        public QueryResponse<T> setResults(ArrayList<T> value) { this.results = value; return this; }
        public HashMap<String,String> getMeta() { return meta; }
        public QueryResponse<T> setMeta(HashMap<String,String> value) { this.meta = value; return this; }
        public ResponseStatus getResponseStatus() { return responseStatus; }
        public QueryResponse<T> setResponseStatus(ResponseStatus value) { this.responseStatus = value; return this; }
    }

    public static class ReadRequest
    {
        public Integer id = null;
        
        public Integer getId() { return id; }
        public ReadRequest setId(Integer value) { this.id = value; return this; }
    }

    public static class CrashDto extends BaseEntity
    {
        public String region = null;
        public String state = null;
        public Date date = null;
        public String type = null;
        
        public String getRegion() { return region; }
        public CrashDto setRegion(String value) { this.region = value; return this; }
        public String getState() { return state; }
        public CrashDto setState(String value) { this.state = value; return this; }
        public Date getDate() { return date; }
        public CrashDto setDate(Date value) { this.date = value; return this; }
        public String getType() { return type; }
        public CrashDto setType(String value) { this.type = value; return this; }
    }

    public static class AutoveloxDto extends BaseEntity
    {
        public Double latitude = null;
        public Double longitude = null;
        public Date date = null;
        public String city = null;
        public String state = null;
        public String region = null;
        
        public Double getLatitude() { return latitude; }
        public AutoveloxDto setLatitude(Double value) { this.latitude = value; return this; }
        public Double getLongitude() { return longitude; }
        public AutoveloxDto setLongitude(Double value) { this.longitude = value; return this; }
        public Date getDate() { return date; }
        public AutoveloxDto setDate(Date value) { this.date = value; return this; }
        public String getCity() { return city; }
        public AutoveloxDto setCity(String value) { this.city = value; return this; }
        public String getState() { return state; }
        public AutoveloxDto setState(String value) { this.state = value; return this; }
        public String getRegion() { return region; }
        public AutoveloxDto setRegion(String value) { this.region = value; return this; }
    }

    public static class QueryDb<T> extends QueryBase
    {
        
    }

    public static class Crash extends BaseEntityWithAutoIncrement
    {
        public String region = null;
        public String state = null;
        public Date date = null;
        public String type = null;
        
        public String getRegion() { return region; }
        public Crash setRegion(String value) { this.region = value; return this; }
        public String getState() { return state; }
        public Crash setState(String value) { this.state = value; return this; }
        public Date getDate() { return date; }
        public Crash setDate(Date value) { this.date = value; return this; }
        public String getType() { return type; }
        public Crash setType(String value) { this.type = value; return this; }
    }

    public static class Autovelox extends BaseEntityWithAutoIncrement
    {
        public Double latitude = null;
        public Double longitude = null;
        public Date date = null;
        public String city = null;
        public String state = null;
        public String region = null;
        
        public Double getLatitude() { return latitude; }
        public Autovelox setLatitude(Double value) { this.latitude = value; return this; }
        public Double getLongitude() { return longitude; }
        public Autovelox setLongitude(Double value) { this.longitude = value; return this; }
        public Date getDate() { return date; }
        public Autovelox setDate(Date value) { this.date = value; return this; }
        public String getCity() { return city; }
        public Autovelox setCity(String value) { this.city = value; return this; }
        public String getState() { return state; }
        public Autovelox setState(String value) { this.state = value; return this; }
        public String getRegion() { return region; }
        public Autovelox setRegion(String value) { this.region = value; return this; }
    }

    public static class BaseEntity
    {
        public Integer id = null;
        
        public Integer getId() { return id; }
        public BaseEntity setId(Integer value) { this.id = value; return this; }
    }

    public static class QueryBase
    {
        @DataMember(Order=1)
        public Integer skip = null;

        @DataMember(Order=2)
        public Integer take = null;

        @DataMember(Order=3)
        public String orderBy = null;

        @DataMember(Order=4)
        public String orderByDesc = null;

        @DataMember(Order=5)
        public String include = null;

        @DataMember(Order=6)
        public String fields = null;

        @DataMember(Order=7)
        public HashMap<String,String> meta = null;
        
        public Integer getSkip() { return skip; }
        public QueryBase setSkip(Integer value) { this.skip = value; return this; }
        public Integer getTake() { return take; }
        public QueryBase setTake(Integer value) { this.take = value; return this; }
        public String getOrderBy() { return orderBy; }
        public QueryBase setOrderBy(String value) { this.orderBy = value; return this; }
        public String getOrderByDesc() { return orderByDesc; }
        public QueryBase setOrderByDesc(String value) { this.orderByDesc = value; return this; }
        public String getInclude() { return include; }
        public QueryBase setInclude(String value) { this.include = value; return this; }
        public String getFields() { return fields; }
        public QueryBase setFields(String value) { this.fields = value; return this; }
        public HashMap<String,String> getMeta() { return meta; }
        public QueryBase setMeta(HashMap<String,String> value) { this.meta = value; return this; }
    }

    public static class BaseEntityWithAutoIncrement
    {
        public Integer id = null;
        
        public Integer getId() { return id; }
        public BaseEntityWithAutoIncrement setId(Integer value) { this.id = value; return this; }
    }

}
