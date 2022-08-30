-- USERS
create sequence user_seq start 1 increment 1;

create table users (
                       id bigint NOT NULL,
                       username varchar(255),
                       password varchar(255),
                       email varchar(255),
                       phone varchar(255),
                       first_name varchar(255),
                       last_name varchar(255),
                       age integer,
                       city varchar(255),
                       role varchar(255),
                       PRIMARY KEY (id)
);
-- BUCKET
create sequence bucket_seq start 1 increment 1;

create table buckets (
                         id bigint NOT NULL,
                         user_id bigint,
                         PRIMARY KEY (id)
);

-- LINK BETWEEN BUCKET AND USER
alter table if exists buckets
    add constraint buckets_fk_user
        foreign key (user_id) references users;


-- CATEGORY
create sequence category_seq start 1 increment 1;

create table categories (
                            id bigint NOT NULL,
                            title varchar(255),
                            PRIMARY KEY (id)
);

-- BRANDS
create sequence brand_seq start 1 increment 1;

CREATE TABLE IF NOT EXISTS brands (
    id bigint NOT NULL,
    title varchar(255),
    CONSTRAINT brands_pkey PRIMARY KEY (id)
);

-- PRODUCTS
create sequence product_seq start 1 increment 1;

CREATE TABLE IF NOT EXISTS products (
    id bigint NOT NULL,
    vendor_code varchar(255),
    brand_id bigint,
    title varchar(255),
    description varchar(500),
    price float8,
    available boolean,
    img_url varchar(255),
    category_id bigint,
    CONSTRAINT products_pkey PRIMARY KEY (id),
    CONSTRAINT products_fk_brand FOREIGN KEY (brand_id)
        REFERENCES public.brands (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT products_fk_category FOREIGN KEY (category_id)
        REFERENCES public.categories (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- LINK BETWEEN PRODUCT AND BRAND
/*alter table if exists products
    add constraint products_fk_brand
        foreign key (brand_id) references brands;*/


-- PRODUCTS IN BUCKET

CREATE TABLE IF NOT EXISTS bucket_items
(
    bucket_id bigint NOT NULL,
    amount bigint NOT NULL,
    product_id bigint NOT NULL,
    CONSTRAINT bucket_items_pkey PRIMARY KEY (bucket_id, product_id),
    CONSTRAINT bucket_products_fk_product FOREIGN KEY (product_id)
        REFERENCES products (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION,
    CONSTRAINT bucket_products_fk_bucket FOREIGN KEY (bucket_id)
        REFERENCES buckets (id) MATCH SIMPLE
        ON update NO ACTION
        ON delete NO ACTION
);

-- ORDERS
create sequence order_seq start 1 increment 1;

create table orders (
                        id bigint NOT NULL,
                        changed timestamp,
                        created timestamp,
                        sum numeric(19, 2),
                        address varchar(255),
                        recipient varchar(255),
                        phone varchar(255),
                        email varchar(255),
                        payment varchar(255),
                        delivery varchar(255),
                        status varchar(255),
                        user_id bigint,
                        primary key (id)
);

alter table if exists orders
    add constraint orders_fk_user
        foreign key (user_id) references users;

-- DETAILS OF ORDER
create sequence order_details_seq start 1 increment 1;

create table orders_details (
                                id bigint NOT NULL,
                                order_id bigint NOT NULL,
                                product_id bigint NOT NULL,
                                amount numeric(19, 2),
                                price numeric(19, 2),
                                primary key (id)
);

alter table if exists orders_details
    add constraint orders_details_fk_order
        foreign key (order_id) references orders;

alter table if exists orders_details
    add constraint orders_details_fk_product
        foreign key (product_id) references products;