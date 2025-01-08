SELECT * FROM book_borrow;

SELECT * FROM book_categories;

SELECT * FROM books;
SELECT *
FROM books
WHERE id = '33491';

select *
from books
where book_category_id = (SELECT max(book_category_id) FROM books);

select *
from books
where name = '"The Collected Works"';

SELECT * FROM settings;

SELECT * FROM user_groups;

SELECT * FROM users;

SELECT *
FROM users
WHERE full_name = 'address';

SELECT * FROM users WHERE id = '19589';