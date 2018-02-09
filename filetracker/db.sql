

  
DROP TABLE IF EXISTS `agent`;
create table `agent` (`id_agent` int(11) NOT NULL AUTO_INCREMENT,
 `user_name` text not null, 
 `password` text not null,
 `student_number` text not null,
 `full_name` text not null,
 PRIMARY KEY (`id_agent`));
 
 ALTER TABLE agent ADD admin boolean NOT NULL default false;

--

Drop table if exists `borrowed`;
CREATE TABLE `borrowed` (
  `id` int(11) NOT NULL,
  `borrower` text,
  `item` text,
  `agent_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `borrowed`
  ADD PRIMARY KEY (`id`);
  
 ALTER TABLE `borrowed`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

ALTER TABLE borrowed ADD COLUMN date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- 
 
 CREATE TABLE `returned` (
  `id` int(11) NOT NULL,
  `borrower` text NOT NULL,
  `item` text NOT NULL,
   `agent_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `returned`
  ADD PRIMARY KEY (`id`);
  
 ALTER TABLE `returned`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

ALTER TABLE returned ADD COLUMN date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


CREATE TABLE `transaction_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item` text NOT NULL,
   `agent_id` int(11) NOT NULL,
   `date_created` timestamp NOT NULL default current_timestamp,
   PRIMARY KEY (`id`)
);
ALTER TABLE transaction_logs ADD borrowed boolean NOT NULL default false;
ALTER TABLE transaction_logs ADD returned boolean NOT NULL default false;



