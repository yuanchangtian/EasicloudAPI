SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tbl_device`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_device`;
CREATE TABLE `tbl_device` (
`device_id`  int(11) NOT NULL AUTO_INCREMENT ,
`user_id`  int(11) NOT NULL ,
`device_active`  int(1) NOT NULL DEFAULT 1 ,
`device_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`device_created`  datetime NULL DEFAULT NULL ,
`device_description`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`device_publicity`  int(11) NULL DEFAULT NULL ,
`device_location`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`device_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1001

;
