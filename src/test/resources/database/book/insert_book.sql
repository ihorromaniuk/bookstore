insert into books (id, title, author, isbn, price, description, cover_image)
values (1,
        'Mr Mercedes',
        'Stephen King',
        '1501125605',
        100,
        'Book about detective and murderer',
        'https://m.media-amazon.com/images/I/91suL+10woL._SL1500_.jpg');
insert into books_categories (book_id, category_id)
values (1, 1);