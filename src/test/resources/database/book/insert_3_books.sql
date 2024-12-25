insert into books (id, title, author, isbn, price, description, cover_image)
values (1,
        'Hobbit',
        'J.R.R. Tolkien',
        '9780261102217',
        500,
        'Book about adventures of hobbit',
        'image url');
insert into books_categories (book_id, category_id)
values (1, 1);
insert into books_categories (book_id, category_id)
values (1, 2);

insert into books (id, title, author, isbn, price, description, cover_image)
values (2,
        'Harry Potter and the Philosopher\'s stone',
        'J.K. Rowling',
        '978-1-4088-5565-2',
        475,
        'Book about wizzard',
        'image url');
insert into books_categories (book_id, category_id)
values (2, 2);

insert into books (id, title, author, isbn, price, description, cover_image)
values (3,
        'Charlie and the Chocolate Factory',
        'Q. Blake',
        '9780241558324',
        400,
        'Book about Willy Wonka',
        'image url');
insert into books_categories (book_id, category_id)
values (3, 1);
