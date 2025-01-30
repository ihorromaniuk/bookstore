insert into books (id, title, author, isbn, price, description, cover_image)
values (1,
        'Bot',
        'Max Kidruk',
        '1500025605',
        200,
        'Ukrainian techno thriller book',
        'cover image');
insert into books_categories (book_id, category_id)
values (1, 3);
