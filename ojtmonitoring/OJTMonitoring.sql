
DROP DATABASE IF EXISTS ojtmonitoring;
CREATE DATABASE IF NOT EXISTS ojtmonitoring;

USE ojtmonitoring;




CREATE TABLE user(id int not null auto_increment,
				  name text,
				  address text,
				  phonenumber text,
				  studentnumber text,
				  teachernumber text,
				  email text,
				  department text,
				  college text,
				  username text,
				  password text,
				  accounttype int,
				  log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
				  primary key(id)
				 );

ALTER TABLE user ADD COLUMN approved boolean default false;
ALTER TABLE user ADD COLUMN approved_by_teacher_id int,
                 ADD COLUMN approved_date date;

CREATE TABLE company_profile(id int not null auto_increment,
							 user_id int,
							 description text,
							 moa_certified boolean default false,
							 does_provide_allowance boolean default false,
							 allowance double precision,
							 ojt_number int,
							 college text,
							 log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
							 primary key(id)
							);


CREATE TABLE resume_details(id int not null auto_increment,
			   user_id int,
			   accomplishments text,
			   interests text,
			   approved boolean default false,
			   ojt_hours_needed double precision,	   
			   updated_by_teacher_id int,
			   teacher_notes text,
			   company_accepted boolean default false,
			   log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			   primary key(id)
);




CREATE TABLE work_experience(id int not null auto_increment,
			     resume_details_id int,
			     company_name text,
			     address text,
			     job_description text,
			     duties_responsibilities text,
			     log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
			    primary key(id) 
			   );


CREATE TABLE educational_background(id int not null auto_increment,
				    resume_details_id int,
				    type int, 
				    name text,
				    address text,
				    from_date date,
				    to_date date,
				    log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
				   primary key(id)
				   );

CREATE TABLE resume_references(id int not null auto_increment,
		     resume_details_id int,
		     name text,
		     address text,
		     phone_number text,
		     occupation text,
		     log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
		     primary key(id)
		     );



CREATE TABLE student_company_selected(id int not null auto_increment,
									  user_id int,
									  company_id int,
									  log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
									  primary key(id)
									 );

CREATE TABLE company_ojt(id int not null auto_increment,
						user_id int,
						company_id int,
						approved_by_teacher_id int,
						log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
						primary key(id)
						);

ALTER TABLE company_ojt ADD COLUMN accepted boolean default false,
						ADD COLUMN accepted_by_company_id int;

ALTER TABLE company_ojt ADD COLUMN accepted_date date;

CREATE TABLE student_notif(id int not null auto_increment,
						   user_id int,
						   message text,
						   deleted boolean default false,
						   deleted_date date,
						   log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
						   primary key(id)
);


CREATE TABLE student_ojt_attendance_log(id int not null auto_increment,
										student_id int,
										company_id int,
										login_date text,
										logout_date text,
										scan_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
										login boolean default false,
										primary key(id)
										);


CREATE TABLE section(id int not null auto_increment,
		     company_id int,
		     no_of_students int,
		     section_name text,
		     created_by_teacher_id int, 
		     log_date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
		     primary key(id)
	            );

ALTER TABLE user ADD COLUMN section_id int;
ALTER TABLE user ADD COLUMN section text;

ALTER TABLE user ADD COLUMN company_name text;
ALTER TABLE user ADD COLUMN company_id int;

ALTER TABLE student_ojt_attendance_log ADD COLUMN agent_id int;
