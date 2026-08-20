SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);

CREATE TABLE public.cart_items (
    quantity integer NOT NULL,
    id uuid NOT NULL,
    product_id uuid,
    user_id uuid
);

CREATE TABLE public.order_items (
    quantity integer NOT NULL,
    id uuid NOT NULL,
    order_id uuid,
    product_id uuid
);

CREATE TABLE public.orders (
    total numeric(38,2),
    created_at timestamp(6) without time zone,
    id uuid NOT NULL,
    user_id uuid,
    payment_method character varying(255),
    shipping_address character varying(255),
    shipping_status character varying(255),
    CONSTRAINT orders_payment_method_check CHECK (((payment_method)::text = ANY (ARRAY[('CASH'::character varying)::text, ('CARD'::character varying)::text, ('ONLINE'::character varying)::text]))),
    CONSTRAINT orders_shipping_status_check CHECK (((shipping_status)::text = ANY (ARRAY[('CREATED'::character varying)::text, ('CONFIRMED'::character varying)::text, ('SHIPPED'::character varying)::text, ('DELIVERED'::character varying)::text, ('CANCELED'::character varying)::text])))
);

CREATE TABLE public.products (
    prescription_required boolean,
    price numeric(38,2),
    stock_quantity integer,
    id uuid NOT NULL,
    category character varying(255),
    description character varying(255),
    image_url character varying(255),
    manufacturer character varying(255),
    name character varying(255)
);

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    middle_name character varying(255),
    password_hash character varying(255),
    phone character varying(255),
    username character varying(255)
);

INSERT INTO public.cart_items (quantity, id, product_id, user_id) VALUES (1, '5038d135-54da-42a5-9212-71b7d85aa6d0', '660e8400-e29b-41d4-a716-446655440001', '0d6afef4-62ca-492b-93d4-72146c6ac924');
INSERT INTO public.cart_items (quantity, id, product_id, user_id) VALUES (1, 'b6d7f1d2-55a5-4f62-a14e-20b7a0479286', '660e8400-e29b-41d4-a716-446655440005', '0d6afef4-62ca-492b-93d4-72146c6ac924');
INSERT INTO public.cart_items (quantity, id, product_id, user_id) VALUES (3, '191f2b81-f0f8-4574-b5f7-a34b4ea7ef18', '660e8400-e29b-41d4-a716-446655440007', '556fa270-39e4-40ec-9d47-0189fdc94870');
INSERT INTO public.cart_items (quantity, id, product_id, user_id) VALUES (1, '5fbb8221-9ca2-4219-8da3-21e7ce455597', '660e8400-e29b-41d4-a716-446655440007', '7587b4f8-8bb1-4e19-8e13-936f419c525b');

INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '03c3cdcb-c03f-48d8-98a2-cd50e56bd5eb', '3f199e34-8fdb-4e96-bbe0-771895a2b899', '660e8400-e29b-41d4-a716-446655440001');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '803065dd-e16a-40e7-bc64-495217592548', '3f199e34-8fdb-4e96-bbe0-771895a2b899', '660e8400-e29b-41d4-a716-446655440005');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '508735f0-95a3-4f31-99ce-bd5e99cdf6f0', '2e2f560d-a00a-4fe4-a0b9-a8672bdfb51f', '660e8400-e29b-41d4-a716-446655440001');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '8ea1487d-3d82-4cb6-9872-f947846eb053', '2e2f560d-a00a-4fe4-a0b9-a8672bdfb51f', '660e8400-e29b-41d4-a716-446655440005');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '71f8ca16-ecfd-4d5e-96e7-24bfb12637e2', 'bef2491b-e772-4aa1-acbd-9987d8db8934', '660e8400-e29b-41d4-a716-446655440001');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '492f7698-2064-4c1b-a4c3-06a4ed563075', 'bef2491b-e772-4aa1-acbd-9987d8db8934', '660e8400-e29b-41d4-a716-446655440005');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (3, '900b58db-2091-4a26-918c-429216ef12f6', '99fecbe8-2e97-4204-89ff-20e331f2cdeb', '660e8400-e29b-41d4-a716-446655440007');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (5, '14d2316a-f7a2-4737-9ffa-9e621f963a08', 'a80d96fd-3079-4ab7-8924-308af1038868', '234d5701-fe27-4e01-b64e-6b61ad5a2cba');
INSERT INTO public.order_items (quantity, id, order_id, product_id) VALUES (1, '5371dacd-017a-4dba-9ee0-4f9c5ee5b57f', '84999132-dab9-4694-8250-d7b87bd03c32', '660e8400-e29b-41d4-a716-446655440007');


INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (630.00, '2025-11-20 19:49:03.857436', '3f199e34-8fdb-4e96-bbe0-771895a2b899', '0d6afef4-62ca-492b-93d4-72146c6ac924', 'CARD', 'фурманный переулок, д. 6, кв. 18', 'CREATED');
INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (630.00, '2025-11-20 21:07:35.209539', '2e2f560d-a00a-4fe4-a0b9-a8672bdfb51f', '0d6afef4-62ca-492b-93d4-72146c6ac924', 'CARD', 'петровский бульвар, д. 2, кв. 111', 'SHIPPED');
INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (630.00, '2025-11-20 21:12:07.927691', 'bef2491b-e772-4aa1-acbd-9987d8db8934', '0d6afef4-62ca-492b-93d4-72146c6ac924', 'CASH', 'улица рождественка, д. 12/1, кв. 11', 'CREATED');
INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (4500.00, '2025-11-21 17:56:02.084113', 'a80d96fd-3079-4ab7-8924-308af1038868', '7587b4f8-8bb1-4e19-8e13-936f419c525b', 'CASH', 'уланский переулок, д. 13с4, кв. 23', 'CREATED');
INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (360.00, '2025-11-21 17:35:28.218631', '99fecbe8-2e97-4204-89ff-20e331f2cdeb', '556fa270-39e4-40ec-9d47-0189fdc94870', 'CASH', 'пресненская набережная, д. 12, кв. 777', 'DELIVERED');
INSERT INTO public.orders (total, created_at, id, user_id, payment_method, shipping_address, shipping_status) VALUES (120.00, '2025-11-21 18:14:15.487872', '84999132-dab9-4694-8250-d7b87bd03c32', '7587b4f8-8bb1-4e19-8e13-936f419c525b', 'CASH', 'улица чаплыгина, д. 1ас1', 'CREATED');


INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 120.00, 14, '660e8400-e29b-41d4-a716-446655440007', 'обезболивающие', 'противовоспалительное средство', '/pictures/ibuprofen.jpeg', 'bayer', 'ибупрофен');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (true, 350.00, 4, '660e8400-e29b-41d4-a716-446655440005', 'антибиотики', 'антибактериальный препарат', '/pictures/amoxicillin.jpg', 'sandoz', 'амоксициллин');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 320.00, 0, '660e8400-e29b-41d4-a716-446655440002', 'витамины', 'высокодозированный витамин с для иммунитета', '/pictures/vitamine-c.jpeg', 'solgar', 'витамин с 500мг');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 220.00, 0, '660e8400-e29b-41d4-a716-446655440004', 'обезболивающие', 'обезболивающее при головной боли', '/pictures/nurofen.jpg', 'reckitt benckiser', 'нурофен экспресс');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 280.00, 6, '660e8400-e29b-41d4-a716-446655440001', 'спазмолитики', 'спазмолитик для быстрого снятия боли', '/pictures/noshpa.jpg', 'chinoin', 'ношпа форте');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 80.00, 24, '660e8400-e29b-41d4-a716-446655440006', 'обезболивающие', 'жаропонижающее и обезболивающее', '/pictures/paracetamol.jpeg', 'фармстандарт', 'парацетамол');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 150.00, 4, '660e8400-e29b-41d4-a716-446655440000', 'обезболивающие', 'быстродействующее обезболивающее средство', '/pictures/aspirin.jpeg', 'bayer', 'аспирин экспресс');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (true, 900.00, 6, '234d5701-fe27-4e01-b64e-6b61ad5a2cba', 'БАД', 'комбинированный антианемический препарат, содержащий двухвалентное железо в виде органической соли железа глюконата гидрата', '/pictures/totema.jpg', 'иннотера шузи', 'тотема');
INSERT INTO public.products (prescription_required, price, stock_quantity, id, category, description, image_url, manufacturer, name) VALUES (false, 190.00, 5, '660e8400-e29b-41d4-a716-446655440003', 'антигистаминные', 'противоаллергическое средство', '/pictures/loratadin.jpg', 'фармстандарт', 'лоратадин');


INSERT INTO public.users (id, email, first_name, last_name, middle_name, password_hash, phone, username) VALUES ('4ef72594-c732-43e1-b027-5a22afd5ed16', 'admin@selderey.ru', 'Администратор', 'Системы', NULL, '$2a$10$yIvwrPNJG.UWcvFQPzNVG.FOlqaFlKIPvmzYRcfOPdri8zmF.AZiC', NULL, 'admin');
INSERT INTO public.users (id, email, first_name, last_name, middle_name, password_hash, phone, username) VALUES ('556fa270-39e4-40ec-9d47-0189fdc94870', 'vkatsap@outlook.com', 'Владимир', 'Кацап', 'Евгеньевич', '$2a$10$1FutH5uKSosGSQHtpmvkDeZ.IhOxTjAN9whPqyWOw.57Svc66./bW', '+7 (911) 911-91-91', 'vkatsap');
INSERT INTO public.users (id, email, first_name, last_name, middle_name, password_hash, phone, username) VALUES ('7587b4f8-8bb1-4e19-8e13-936f419c525b', 'lleftalittle@gmail.com', 'Юлия', 'Минакова', 'Дмитриевна', '$2a$10$xyYEhIAFfGVRmW8bQ5DQf.bEXFmy4CyotaOVKFQkiOpbBE5gFYDAW', '+7 (920) 231-31-69', 'lleftalittle');
INSERT INTO public.users (id, email, first_name, last_name, middle_name, password_hash, phone, username) VALUES ('0d6afef4-62ca-492b-93d4-72146c6ac924', 'lidiya1962@mail.ru', 'Лилия', 'Карданова', 'севастьяновна', '$2a$10$RDm.kTDiPuqulUHVUqjC6e8Ri1r7elKpNuhdaqlNm0zwHQUr3ubZi', '+7 (934) 399-18-44', 'lidiya1962');
INSERT INTO public.users (id, email, first_name, last_name, middle_name, password_hash, phone, username) VALUES ('c5c4da3b-392e-4137-b01f-9cda19f2bcde', 'un4va1lablxx@gmail.com', 'Иван', 'Белкин', 'Витальевич', '$2a$10$n2F.eZU1IrYoPgA.R/ohUu1/CjG9X/TPI86LSkv9LNvJX28t84zPG', '+7 (192) 011-22-33', 'user');


ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_user_id_product_id_key UNIQUE (user_id, product_id);


ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT fk1re40cjegsfvw58xrkdp6bac6 FOREIGN KEY (product_id) REFERENCES public.products(id);


ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT fk709eickf3kc0dujx3ub9i7btf FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fkbioxgbv59vetrxe0ejfubep1w FOREIGN KEY (order_id) REFERENCES public.orders(id);


ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT fkocimc7dtr037rh4ls4l95nlfi FOREIGN KEY (product_id) REFERENCES public.products(id);
