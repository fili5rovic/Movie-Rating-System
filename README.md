# Movie Rating System

This is a Java project that directly communicates with a Microsoft SQL Server database using JDBC. It implements a comprehensive movie rating and recommendation system where users can rate movies, create watchlists, and receive personalized recommendations based on their preferences.

> The primary motivation for this project was to explore the complexities of database systems, specifically focusing on advanced SQL features like **stored procedures, functions, and triggers**, along with JDBC integration for Java applications.

> This project is an assignment for the "Database Software Tools" course at the University of Belgrade School of Electrical Engineering, majoring in Software Engineering. For detailed assignment instructions, please refer to the [respective file](instructions.pdf).

## Features

### Core Functionality
- **User Management** - Register and manage user profiles with reward tracking
- **Movie Catalog** - Comprehensive movie database with titles, directors, and metadata
- **Genre & Tag System** - Movies can belong to multiple genres and have descriptive tags
- **Rating System** - Users can rate movies on a scale of 1-10
- **Watchlists** - Personal movie watchlists for each user

### Advanced Features
- **Extreme Rating Prevention** - Automatic blocking of excessive extreme ratings (1 or 10) in genres where users lack sufficient neutral ratings
- **Smart Recommendations** - Personalized movie suggestions based on:
  - Favorite genres (average rating ≥ 8)
  - Popular films (≥4 ratings, average ≥ 7.5)
  - Hidden gems (<4 ratings, average ≥ 9)
- **User Rewards** - Reward system for users who rate underappreciated movies in their favorite genres
- **User Specialization** - Automatic detection of user expertise in specific tags/themes
- **User Profiling** - Classification of users as "curious", "focused", or "undefined" based on rating patterns

## Technical Implementation

### Database Components
- **Stored Procedures**:
  - `SP_RECOMMENDATION` - Generates personalized movie recommendations
  - `SP_REWARD_USER_PROC` - Handles user reward calculations and distribution
- **Functions**:
  - `F_USER_SPEC` - Identifies user specializations based on high-rated tags
  - `F_USER_DESC` - Classifies users as curious, focused, or undefined
- **Triggers**:
  - `TR_BLOCK_EXTREME_RATINGS` - Enforces rating restrictions to prevent rating abuse

### Database Schema
Key tables include:
- `Korisnik` (Users) - User information and reward count
- `Film` (Movies) - Movie details and directors
- `Zanr` (Genres) - Movie genres
- `Oznaka` (Tags) - Descriptive tags for movies
- `Ocena` (Ratings) - User ratings for movies
- `Lista` (Watchlists) - User personal watchlists
- `FilmZanrLink` / `FilmOznakaLink` - Many-to-many relationship tables

### Technologies Used
- **Backend**: Java with JDBC
- **Database**: Microsoft SQL Server
- **SQL Features**: Stored Procedures, User-Defined Functions, Triggers, CTEs, Temporary Tables
- **Constraints**: Referential integrity with `ON UPDATE CASCADE`, `ON DELETE NO ACTION`

## Key Business Rules

### Rating Restrictions
Users cannot give extreme ratings (1 or 10) in a genre if they already have:
- More than 3 extreme ratings AND
- Fewer than 3 neutral ratings (6, 7, 8) in that genre

### Recommendation Criteria
Movies are recommended if they:
- Belong to user's favorite genres (avg rating ≥ 8)
- User hasn't rated or added to watchlist
- Meet either:
  - ≥4 ratings with average ≥ 7.5, OR
  - <4 ratings with average ≥ 9 (hidden gems)

### Reward System
Users earn rewards when they rate movies that:
- Belong to their favorite genres
- Have global average rating < 6 (excluding user's own rating)
- User has rated at least 10 movies total

### User Classification
- **Undefined**: Rated fewer than 10 movies
- **Focused**: Rated ≥10 movies covering <10 different tags
- **Curious**: Rated ≥10 movies covering ≥10 different tags

