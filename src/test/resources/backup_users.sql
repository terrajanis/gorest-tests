PGDMP         (        	        z            test    14.2    14.2     ?           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            ?           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            ?           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            ?           1262    16558    test    DATABASE     a   CREATE DATABASE test WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Russian_Russia.1251';
    DROP DATABASE test;
                postgres    false            ?            1259    16559    users    TABLE     ?   CREATE TABLE public.users (
    name character varying(256),
    email character varying(256),
    gender character varying(50),
    status character varying(50),
    id bigint NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            ?            1259    16566    users_id_seq    SEQUENCE     ?   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public          postgres    false    209            ?           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
          public          postgres    false    210            \           2604    16576    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    210    209            ?          0    16559    users 
   TABLE DATA           @   COPY public.users (name, email, gender, status, id) FROM stdin;
    public          postgres    false    209   Y
       ?           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 6, true);
          public          postgres    false    210            ^           2606    16578    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    209            ?   ?   x?u?A? F?5????0??=?Ҡ!)?h??5??(???[R?s???A ???3Nך?6??j.??-??????}??k??>oƿN?4z?aO?؅yX??"R???kڃL?â???Z?Sd?     