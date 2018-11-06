-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE public.users
(
  id bigint NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  name character varying(255),
  age integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.users
  OWNER TO postgres;