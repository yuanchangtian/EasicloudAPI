-- ----------------------------
-- Table structure for `tbl_sensor`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_sensor`;
CREATE TABLE `tbl_sensor` (
`sensor_id`  int(11) NOT NULL AUTO_INCREMENT ,
`device_id`  int(11) NOT NULL ,
`sensor_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sensor_description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`sensor_imgurl`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`sensor_data`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公用信息' ,
`sensor_values`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据值' ,
PRIMARY KEY (`sensor_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=4

;