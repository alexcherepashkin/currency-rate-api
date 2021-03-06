CREATE SEQUENCE rate_id_seq INCREMENT 1 START 1;

CREATE TABLE curr_rate (
    id int8 NOT NULL DEFAULT nextval('rate_id_seq'),
    currency varchar(55) NOT NULL,
    base_currency varchar(55) NOT NULL,
    buy varchar(55) NOT NULL,
    sale varchar(55) NOT NULL,
    date_time timestamp,
    source varchar(55) NOT NULL,
    PRIMARY KEY (id)
);
