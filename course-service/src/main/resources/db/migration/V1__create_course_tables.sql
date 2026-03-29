CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    slug VARCHAR(100) UNIQUE,
    icon_url VARCHAR(500),
    parent_id UUID REFERENCES categories(id)
);

CREATE TABLE courses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    instructor_id UUID NOT NULL,
    instructor_name VARCHAR(150),
    category_id UUID REFERENCES categories(id),
    price NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    discount_price NUMERIC(10,2),
    thumbnail_url VARCHAR(500),
    preview_video_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    level VARCHAR(20),
    average_rating DOUBLE PRECISION DEFAULT 0.0,
    total_reviews INTEGER DEFAULT 0,
    total_enrollments INTEGER DEFAULT 0,
    duration_hours DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP
);

CREATE TABLE course_requirements (
    course_id UUID REFERENCES courses(id) ON DELETE CASCADE,
    requirement TEXT
);

CREATE TABLE course_objectives (
    course_id UUID REFERENCES courses(id) ON DELETE CASCADE,
    objective TEXT
);

CREATE INDEX idx_courses_instructor_id ON courses(instructor_id);
CREATE INDEX idx_courses_status ON courses(status);
CREATE INDEX idx_courses_category_id ON courses(category_id);

-- categories
INSERT INTO categories (name, description, slug) VALUES
('Programming', 'Learn programming and development', 'programming'),
('Data Science', 'Data analysis and machine learning', 'data-science'),
('Design', 'UI/UX and graphic design', 'design'),
('Business', 'Business and entrepreneurship', 'business'),
('Marketing', 'Digital marketing and SEO', 'marketing');
