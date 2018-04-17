CREATE TABLE `location` (

  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

  `name` varchar(255) NULL COMMENT '中心名称',

  PRIMARY KEY (`id`)

);

CREATE TABLE `committees` (

  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

  `lid` int NULL COMMENT '中心序号',

  `name` varchar(255) NULL COMMENT '居委会名称',

  PRIMARY KEY (`id`)

);

CREATE TABLE `user` (

  `id` int NOT NULL AUTO_INCREMENT COMMENT '序号',

  `lid` int NULL COMMENT '中心序号',

  `name` varchar(255) CHARACTER SET utf8 NULL COMMENT '人员姓名',

  `weixin` varchar(255) CHARACTER SET utf8 NULL COMMENT '企业微信',

  `login` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录名称',

  `pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',

  `state` int NULL COMMENT '0停用1启用',

  PRIMARY KEY (`id`)

);



CREATE TABLE `person` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`tid` int(11) NULL COMMENT '人员类别',

`lid` int(11) NULL COMMENT '中心序号',

`cid` int(11) NULL COMMENT '居委会序号',

`jid` int(11) NULL COMMENT '岗位序号',

`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '证件号码',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',

`sex` int(11) NULL COMMENT '1男2女',

`birth` date NULL COMMENT '出生日期',

`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',

`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',

`bank` varchar(255) CHARACTER SET utf8 NULL COMMENT '银行卡号',

`company` varchar(255) CHARACTER SET utf8 NULL COMMENT '失业前所在单位',

`time_out` date NULL COMMENT '失业时间',

`time_regist` date NULL COMMENT '城镇登记失业时间',

`marriage` int(11) NULL COMMENT '1未婚2已婚3离异4丧偶',

`state` int(11) NULL COMMENT '0未享受1正在享受',

`delay` int(11) NULL COMMENT '0不延期1延期',

`remark` varchar(999) CHARACTER SET utf8 NULL COMMENT '备注',

PRIMARY KEY (`id`)

);



CREATE TABLE `type` (

`id` int NOT NULL AUTO_INCREMENT COMMENT '序号',

`category` int(11) NULL COMMENT '1灵活就业2公益岗位3企业吸纳',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '人员类别名称',

PRIMARY KEY (`id`)

);

CREATE TABLE `job` (

  `id` int NOT NULL AUTO_INCREMENT COMMENT '序号',

  `name` varchar(255) CHARACTER SET utf8 NULL COMMENT '岗位名称',

  PRIMARY KEY (`id`)

);


CREATE TABLE `family` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`pid` int(11) NULL COMMENT '人员序号',

`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '证件号码',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',

`sex` int(11) NULL COMMENT '1男2女',

`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',

`identity` int(11) NULL COMMENT '1夫2妻3子4女5父6母7兄弟8姐妹',

`marriage` int(11) NULL COMMENT '1未婚2已婚3离异4丧偶',

`birth` date NULL COMMENT '出生日期',

`company` varchar(255) CHARACTER SET utf8 NULL COMMENT '原（现）工作（学习）单位',

`time_regist` date NULL COMMENT '城镇登记失业时间',

`state` int(11) NULL COMMENT '0注销1激活',

`remark` varchar(999) CHARACTER SET utf8 NULL COMMENT '备注',

PRIMARY KEY (`id`)

);



CREATE TABLE `changePerson` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`pid` int(11) NULL COMMENT '人员序号',

`uid` int(11) NULL COMMENT '用户序号',

`type` int(11) NULL COMMENT '1新增2信息变更3注销4激活',

`time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',

`before` varchar(999) CHARACTER SET utf8 NULL COMMENT '之前的信息',

`after` varchar(999) CHARACTER SET utf8 NULL COMMENT '之后的信息',

`reason` varchar(999) CHARACTER SET utf8 NULL COMMENT '变更原因',

PRIMARY KEY (`id`)

);



CREATE TABLE `changeFamily` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`fid` int(11) NULL COMMENT '家庭成员序号',

`uid` int(11) NULL COMMENT '用户序号',

`type` int(11) NULL COMMENT '1新增2信息变更3注销4激活',

`time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',

`before` varchar(999) CHARACTER SET utf8 NULL COMMENT '之前的信息',

`after` varchar(999) CHARACTER SET utf8 NULL COMMENT '之后的信息',

`reason` varchar(999) CHARACTER SET utf8 NULL COMMENT '变更原因',

PRIMARY KEY (`id`)

);

CREATE TABLE `message` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`uid` int(11) NULL COMMENT '用户序号',

`time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '发送时间',

`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '消息内容',

`state` int(11) NULL COMMENT '0失败1成功',

PRIMARY KEY (`id`)

);

CREATE TABLE `security` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`lid` int(11) NULL COMMENT '中心序号',

`type` int(11) NULL COMMENT '身份：0本人1家属',

`sfzhm` varchar(255) CHARACTER SET utf8 NULL COMMENT '证件号码',

`xm` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',

`dwbh` varchar(255) CHARACTER SET utf8 NULL COMMENT '单位编号',

`dwmc` varchar(255) CHARACTER SET utf8 NULL COMMENT '单位名称',

`zjny` varchar(255) CHARACTER SET utf8 NULL COMMENT '增减年月',

`jfrylb` varchar(255) CHARACTER SET utf8 NULL COMMENT '缴费人员类别：A8灵活就业人员A1城合制',

`ylrylb` varchar(255) CHARACTER SET utf8 NULL COMMENT '医疗人员类别：0在职职工2退休职工4减员职工',

`zglb` varchar(255) CHARACTER SET utf8 NULL COMMENT '职工类别：A在职人员C退休人员F中断人员',

`state` int(11) NULL COMMENT '0不存在1无权查询2正常3查询出错',

PRIMARY KEY (`id`)

);


ALTER TABLE `user` ADD CONSTRAINT `location_user` FOREIGN KEY (`lid`) REFERENCES `location` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_type` FOREIGN KEY (`tid`) REFERENCES `type` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_committees` FOREIGN KEY (`cid`) REFERENCES `committees` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_location` FOREIGN KEY (`lid`) REFERENCES `location` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_job` FOREIGN KEY (`jid`) REFERENCES `job` (`id`);

ALTER TABLE `family` ADD CONSTRAINT `family_person` FOREIGN KEY (`pid`) REFERENCES `person` (`id`);

ALTER TABLE `changePerson` ADD CONSTRAINT `changePerson_person` FOREIGN KEY (`pid`) REFERENCES `person` (`id`);

ALTER TABLE `changePerson` ADD CONSTRAINT `changePerson_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);

ALTER TABLE `changeFamily` ADD CONSTRAINT `changeFamily_family` FOREIGN KEY (`fid`) REFERENCES `family` (`id`);

ALTER TABLE `changeFamily` ADD CONSTRAINT `changeFamily_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);

ALTER TABLE `message` ADD CONSTRAINT `message_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);

ALTER TABLE `security` ADD CONSTRAINT `security_location` FOREIGN KEY (`lid`) REFERENCES `location` (`id`);
