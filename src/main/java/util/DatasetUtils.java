package util;

public final class DatasetUtils {

/*    public static final StructType =
    new StructType(StructField("STATION", StringType,true), StructField("DATE",StringType,true), StructField("LATITUDE", StringType,true), StructField("LONGITUDE",StringType,true),
    StructField("ELEVATION", StringType,true), StructField("NAME",StringType,true), StructField("TEMP", StringType,true), StructField("TEMP_ATTRIBUTES",StringType,true),
    StructField("DEWP", StringType,true), StructField("DEWP_ATTRIBUTES",StringType,true), StructField("SLP", StringType,true), StructField("SLP_ATTRIBUTES",StringType,true),
    StructField("STP", StringType,true), StructField("STP_ATTRIBUTES",StringType,true), StructField("VISIB", StringType,true), StructField("VISIB_ATTRIBUTES",StringType,true),
    StructField("WDSP", StringType,true), StructField("WDSP_ATTRIBUTES",StringType,true), StructField("MXSPD", StringType,true), StructField("GUST",StringType,true),
    StructField("MAX", StringType,true), StructField("MAX_ATTRIBUTES",StringType,true), StructField("MIN", StringType,true), StructField("MIN_ATTRIBUTES",StringType,true),
    StructField("PRCP", StringType,true), StructField("PRCP_ATTRIBUTES",StringType,true),
    StructField("SNDP", StringType,true), StructField("FRSHTT",DoubleType,true));*/


    public static final String INPUT_SCHEMA =
            "STATION integer," +
                    "DATE string," +
                    "LATITUDE decimal(21,8)," +
                    "LONGITUDE decimal(21,8)," +
                    "ELEVATION decimal(19,2)," +
                    "NAME string," +
                    "TEMP decimal(12,2)," +
                    "TEMP_ATTRIBUTES integer," +
                    "DEWP decimal(12,2)," +
                    "DEWP_ATTRIBUTES integer," +
                    "SLP decimal(19,2)," +
                    "SLP_ATTRIBUTES integer," +
                    "STP decimal(19,2)," +
                    "STP_ATTRIBUTES integer," +
                    "VISIB decimal(19,2)," +
                    "VISIB_ATTRIBUTES integer," +
                    "WDSP decimal(19,2)," +
                    "WDSP_ATTRIBUTES integer," +
                    "MXSPD decimal(19,2)," +
                    "GUST decimal(19,2)," +
                    "MAX decimal(19,2)," +
                    "MAX_ATTRIBUTES string," +
                    "MIN decimal(19,2)," +
                    "MIN_ATTRIBUTES string," +
                    "PRCP decimal(19,2)," +
                    "PRCP_ATTRIBUTES string," +
                    "SNDP decimal(19,2)," +
                    "FRSHTT decimal(19,2)";
}
