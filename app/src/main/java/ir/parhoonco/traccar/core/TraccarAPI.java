package ir.parhoonco.traccar.core;

import java.util.List;

import ir.parhoonco.traccar.core.model.Empty;
import ir.parhoonco.traccar.core.model.api.Address;
import ir.parhoonco.traccar.core.model.api.Command;
import ir.parhoonco.traccar.core.model.api.Credit;
import ir.parhoonco.traccar.core.model.api.Device;
import ir.parhoonco.traccar.core.model.api.DeviceConfig;
import ir.parhoonco.traccar.core.model.api.DeviceInfo;
import ir.parhoonco.traccar.core.model.api.Event;
import ir.parhoonco.traccar.core.model.api.Insurance;
import ir.parhoonco.traccar.core.model.api.MessageCreate;
import ir.parhoonco.traccar.core.model.api.Messages;
import ir.parhoonco.traccar.core.model.api.Payment;
import ir.parhoonco.traccar.core.model.api.Pm;
import ir.parhoonco.traccar.core.model.api.Positions;
import ir.parhoonco.traccar.core.model.api.TicketCreate;
import ir.parhoonco.traccar.core.model.api.Tickets;
import ir.parhoonco.traccar.core.model.api.PmConfig;
import ir.parhoonco.traccar.core.model.api.Position;
import ir.parhoonco.traccar.core.model.api.Verify;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mao on 9/3/2016.
 */
public interface TraccarAPI {

    @POST("users/verify")
    Call<Empty> verifyPhone(@Query("userid") String phone);

    @POST("users/clientloginbycode")
    Call<Verify> loginByCode(@Query("userid") String phone , @Query("vercode") String vercode
            , @Query("clientid") String clientid , @Query("clienttype") String clienttype);

    @POST("users/clientloginbykey")
    Call<Verify> loginByKey(@Query("userid") String phone , @Query("apikey") String api_key
            , @Query("clientid") String clientid , @Query("clienttype") String clienttype);

    @GET("devices/lastposition")
    Call<Position> getLastPosition(@Query("imei") String imei);

    @GET("pms/config")
    Call<List<PmConfig>> getPmConfig(@Query("imei") String imei);

    @POST("pms/config")
    Call<Empty> setPmConfig(@Query("imei") String imei , @Query("pmkey") String pmkey
            , @Query("timethreshold") int time , @Query("distancethreshold") int distance);

    @GET("tickets/get")
    Call<Tickets> getTickets(@Query("offset") int offset , @Query("limit") int limit);

    @POST("tickets/add")
    Call<TicketCreate> createTicket(@Query("subject") String subject , @Query("body") String body , @Query("receiverid") String receiverId);

    @GET("tickets/getmessages")
    Call<Messages> getMessages(@Query("ticketid") int ticketId , @Query("offset") int offset , @Query("limit") int limit);

    @POST("tickets/addmessage")
    Call<MessageCreate> addMessage(@Query("ticketid") int ticketId , @Query("body") String body);

    @POST("tickets/markread")
    Call<Empty> markRead(@Query("messageid") int messageId);

    @GET("pms/get")
    Call<List<Pm>> getPm(@Query("imei") String imei , @Query("pmkey") String pmkey , @Query("time") long time);

    @POST("devices/config")
    Call<Empty> setDeviceConfig(@Query("imei") String imei , @Query("protectionmode") boolean isProtection , @Query("autooff") boolean auto_off,
                                @Query("minvoltage") double min_voltage , @Query("maxspeed") int max_speed , @Query("autogeofence") boolean auto_geo,
                                @Query("minangle") String minangle , @Query("minperiod") String minperiod , @Query("sendperiod") String sendperiod,
                                @Query("mindistance") String min_dist);

    @GET("devices/getdevice")
    Call<Device> getDevice(@Query("imei") String imei);

    @POST("users/addtogroup")
    Call<Empty> addUserToGroup(@Query("userid") String userid , @Query("imei") String imei);

    @POST("users/deletefromgroup")
    Call<Empty> removeUserFromGroup(@Query("userid") String userid , @Query("imei") String imei);

    @GET("insurances/getinsurance")
    Call<Insurance> getInsurance(@Query("imei") String imei);

    @GET("devices/positions")
    Call<Positions> getPositions(@Query("imei") String imei , @Query("start") long start , @Query("end") long end , @Query("limit") int limit , @Query("offset") int offset);

    @GET("events/getdevice")
    Call<List<Event>> getEvents(@Query("imei") String imei , @Query("starttime") long start);

    @GET("payments/getdevice")
    Call<List<Payment>> getPayments(@Query("imei") String imei);

    @GET("devices/credit")
    Call<Credit> getCredit(@Query("imei") String imei);

    @GET("devices/config")
    Call<DeviceConfig> getDeviceConfig(@Query("imei") String imei);

    @POST("users/setmaster")
    Call<Empty> setMaster(@Query("imei") String imei , @Query("userid") String user_id , @Query("emptygroup") boolean delete_group);

    @GET("commands/getdevice")
    Call<List<Command>> getCommands(@Query("imei") String imei , @Query("starttime") long start);

    @GET("devices/info")
    Call<DeviceInfo> getDeviceInfo(@Query("imei") String imei);

    @POST("pms/deleteconfig")
    Call<Empty> deletePmConfig(@Query("imei") String imei , @Query("pmkey") String pmkey);

    @POST("insurances/add")
    Call<Empty> addInsurance(@Query("imei") String imei , @Query("firstname") String firstname , @Query("lastname") String lastname , @Query("nationalcode") String nationalcode , @Query("address") String address);

    @GET("devices/positionaddress")
    Call<Address> getAddress(@Query("positionid") String positionid);

    @POST("commands/add")
    Call<Empty> addCommand(@Query("imei") String imei , @Query("type") String type , @Query("needexecute") boolean needexecute , @Query("attributes") String attr);
}
