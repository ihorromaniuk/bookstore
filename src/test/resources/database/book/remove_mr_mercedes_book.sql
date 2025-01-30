delete from books_categories
       where book_id = (select id from books where title = 'Mr Mercedes');
delete from books where title = 'Mr Mercedes';
