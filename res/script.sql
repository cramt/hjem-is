CREATE TABLE storage_plans
(
    id   INT identity (1,1),
    name VARCHAR(MAX) NOT NULL,
    primary key (id)
)

CREATE TABLE storage_meta_data
(
    id                     INT identity (1,1),
    percent_inventory_cost FLOAT NOT NULL,
    storage_plan_id        INT   NOT NULL,
    primary key (id),
    foreign key (storage_plan_id) references storage_plans (id) on delete cascade
)

CREATE TABLE periodic_plans
(
    id              INT identity (1,1),
    start_period    INT NOT NULL,
    end_period      INT NOT NULL,
    storage_plan_id INT NOT NULL,
    primary key (id),
    foreign key (storage_plan_id) references storage_plans (id) on delete cascade
)

CREATE TABLE suppliers
(
    id             INT identity (1,1),
    delivery_speed INT          NOT NULL,
    delivery_price INT          NOT NULL,
    name           VARCHAR(MAX) NOT NULL,
    primary key (id)
)

CREATE TABLE storage_orders
(
    id               INT identity (1,1),
    sent_data        DATE,
    tracking_id      VARCHAR(35),
    seasonal_plan_id INT NOT NULL,
    supplier_id      INT NOT NULL,
    primary key (id),
    foreign key (seasonal_plan_id) references periodic_plans (id) on delete cascade,
    foreign key (supplier_id) references suppliers (id) on delete cascade
)

CREATE TABLE products
(
    id   INT identity (1,1),
    cost INT          NOT NULL,
    name VARCHAR(MAX) NOT NULL,
    primary key (id)
)

CREATE TABLE product_lines
(
    id               INT identity (1,1),
    amount           INT NOT NULL,
    product_id       INT NOT NULL,
    storage_order_id INT,
    primary key (id),
    foreign key (product_id) references products (id) on delete cascade,
    foreign key (storage_order_id) references storage_orders (id) on delete cascade
)

CREATE TABLE products_supplier
(
    product_id  INT NOT NULL,
    supplier_id INT NOT NULL,
    foreign key (product_id) references products (id) on delete cascade,
    foreign key (supplier_id) references suppliers (id) on delete cascade
)

CREATE TABLE periodic_plans_products_map
(
    product_id       INT NOT NULL,
    amount           INT NOT NULL,
    periodic_plan_id INT NOT NULL,
    foreign key (product_id) references products (id) on delete cascade,
    foreign key (periodic_plan_id) references periodic_plans (id) on delete cascade
)