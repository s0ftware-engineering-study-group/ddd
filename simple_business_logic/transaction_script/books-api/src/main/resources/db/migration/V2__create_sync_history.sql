create table sync_history (
    id serial primary key,
    number_of_books int,
    sync_at date not null
);
