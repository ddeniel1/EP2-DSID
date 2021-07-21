package util;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public final class DatasetUtils {

    public static final StructType schema = DataTypes.createStructType(new StructField[]{
            DataTypes.createStructField("STATION", DataTypes.LongType, true),
            DataTypes.createStructField("DATE", DataTypes.TimestampType, true),
            DataTypes.createStructField("LATITUDE", DataTypes.StringType, true),
            DataTypes.createStructField("LONGITUDE", DataTypes.StringType, true),
            DataTypes.createStructField("ELEVATION", DataTypes.DoubleType, true),
            DataTypes.createStructField("NAME", DataTypes.StringType, true),
            DataTypes.createStructField("TEMP", DataTypes.DoubleType, true),
            DataTypes.createStructField("TEMP_ATTRIBUTES", DataTypes.DoubleType, true),
            DataTypes.createStructField("DEWP", DataTypes.DoubleType, true),
            DataTypes.createStructField("DEWP_ATTRIBUTES", DataTypes.DoubleType, true),
            DataTypes.createStructField("SLP", DataTypes.DoubleType, true),
            DataTypes.createStructField("SLP_ATTRIBUTES", DataTypes.DoubleType, true),
            DataTypes.createStructField("STP", DataTypes.DoubleType, true),
            DataTypes.createStructField("STP_ATTRIBUTES", DataTypes.DoubleType, true),
            DataTypes.createStructField("VISIB", DataTypes.DoubleType, true),
            DataTypes.createStructField("VISIB_ATTRIBUTES", DataTypes.DoubleType, true),
            DataTypes.createStructField("WDSP", DataTypes.DoubleType, true),
            DataTypes.createStructField("WDSP_ATTRIBUTES", DataTypes.StringType, true),
            DataTypes.createStructField("MXSPD", DataTypes.DoubleType, true),
            DataTypes.createStructField("GUST", DataTypes.DoubleType, true),
            DataTypes.createStructField("MAX", DataTypes.DoubleType, true),
            DataTypes.createStructField("MAX_ATTRIBUTES", DataTypes.StringType, true),
            DataTypes.createStructField("MIN", DataTypes.DoubleType, true),
            DataTypes.createStructField("MIN_ATTRIBUTES", DataTypes.StringType, true),
            DataTypes.createStructField("PRCP", DataTypes.DoubleType, true),
            DataTypes.createStructField("PRCP_ATTRIBUTES", DataTypes.StringType, true),
            DataTypes.createStructField("SNDP", DataTypes.DoubleType, true),
            DataTypes.createStructField("FRSHTT", DataTypes.StringType, true),
    });


//    public static final String INPUT_SCHEMA =
//            "STATION integer," +
//                    "DATE string," +
//                    "LATITUDE decimal(21,8)," +
//                    "LONGITUDE decimal(21,8)," +
//                    "ELEVATION decimal(19,2)," +
//                    "NAME string," +
//                    "TEMP decimal(12,2)," +
//                    "TEMP_ATTRIBUTES integer," +
//                    "DEWP decimal(12,2)," +
//                    "DEWP_ATTRIBUTES integer," +
//                    "SLP decimal(19,2)," +
//                    "SLP_ATTRIBUTES integer," +
//                    "STP decimal(19,2)," +
//                    "STP_ATTRIBUTES integer," +
//                    "VISIB decimal(19,2)," +
//                    "VISIB_ATTRIBUTES integer," +
//                    "WDSP decimal(19,2)," +
//                    "WDSP_ATTRIBUTES integer," +
//                    "MXSPD decimal(19,2)," +
//                    "GUST decimal(19,2)," +
//                    "MAX decimal(19,2)," +
//                    "MAX_ATTRIBUTES string," +
//                    "MIN decimal(19,2)," +
//                    "MIN_ATTRIBUTES string," +
//                    "PRCP decimal(19,2)," +
//                    "PRCP_ATTRIBUTES string," +
//                    "SNDP decimal(19,2)," +
//                    "FRSHTT decimal(19,2)";
}
