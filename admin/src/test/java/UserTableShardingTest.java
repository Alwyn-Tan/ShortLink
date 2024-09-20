public class UserTableShardingTest {
    public static final String SQL = "CREATE TABLE `user` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `username` varchar(256) DEFAULT NULL,\n" +
            "  `password` varchar(512) DEFAULT NULL,\n" +
            "  `phone` varchar(128) DEFAULT NULL,\n" +
            "  `mail` varchar(512) DEFAULT NULL,\n" +
            "  `delete_time` datetime DEFAULT NULL,\n" +
            "  `create_time` datetime DEFAULT NULL,\n" +
            "  `update_time` datetime DEFAULT NULL,\n" +
            "  `del_flag` tinyint(1) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `idx_unique_username` (`username`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1819996892344385539 DEFAULT CHARSET=utf8";

    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
            System.out.printf((SQL) + "%n", i);
        }
    }
}
