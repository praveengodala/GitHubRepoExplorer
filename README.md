# Token Setup Instructions
1. **Generate a GitHub Personal Access Token**:
    - Go to GitHub.com → Settings → Developer Settings → Personal Access Tokens → Tokens (classic)
    - Generate new token
    - Select required scopes (e.g. `repo` access)
    - Copy the generated token

2. **Add the token to the project**:
   - Add the line below to `local.properties`
   - `github_token = your_token_here`
   - Run `app`

3. Idea here is to safely add `github_token` to `local.properties` (part of `.gitignore`) then expose it during runtime as a regular resource value via auto generated `build/gradleResValues.xml` (part of `.gitignore`)

# Focus
- Clean, scalable, and easy-to-read architecture.
- Core functionality with efficient API calls.
- Asynchronous processing for parallel API requests.
- Comprehensive testing.
- User-friendly UI.
- Error Handling.
- Completion within optimal time.

# Tech Stack
- **Kotlin**: Primary programming language.
- **Jetpack Compose**: UI framework.
- **Coroutines**: For asynchronous and parallel handling of API requests.
- **Retrofit / OkHttp**: For network requests.
- **Gson**: For JSON parsing.
- **Hilt**: For dependency injection.
- **LiveData**: For observing and managing UI data.
- **Mockk & Junit**: For unit testing

# Architecture
- **MVVM** (Model-View-ViewModel) with **Clean Architecture** principles.

## Reasons for using this approach:
- **Separation of Concerns / Single Responsibility Principle / Dependency Inversion**:
    - Layers are clearly separated, making it easier to maintain and extend.
    - Outer layers depend on inner layers, but not vice versa (e.g., the data layer can depend on the domain layer, but not the other way around).
    - MVVM ensures that the UI layer remains lightweight, moving UI-related logic to the ViewModel and non-UI business logic to the Domain/Data layer.
    - ViewModel survives configuration changes

- **Testability**:
    - This architecture makes it easier to mock and test across layers, improving the reliability of the application.

- **Scalability**:
    - The separation of concerns and modular structure enhance the ability to scale the application in both features and complexity.

# Trade-offs / Considerations
1. **Authenticated users vs Non-authenticated users**
    - I need to make 101 API calls (1 for top 100 repositories + 1 for top contributor per repo)
    - Non-authenticated users are rate-limited to 60 requests per hour.
    - To mitigate this, I added a provision to add access token and work as authorized User to extend the rate limit (5000 requests per hour).

2. **Pagination vs fetching all data at once?**
    - For simplicity and within the time constraints while satisfying the core functionality, I opted to fetch all 100 repositories and their top contributors in a single request
    - **Concurrency**: I used the `async` coroutine builder to launch 100 concurrent coroutines.
    - **Optimized for network**: I used the `IO` dispatcher to optimize for network-bound tasks, ensuring parallel execution of coroutines.
    - **Caching**: I added OkHttp caching to leverage `Cache-Control` and `ETag` headers supported by GitHub's REST API. OkHttp automatically handles caching based on the response headers and request configurations.
    - **Paging3** is the recommended library for paging in android brings in a few components. This library fits well in to Clean architecture layers (respective classes can be placed in repository, viewmodel and ui layers) making it easier to organize, integrate and test.

3. **LiveData vs Flows**:
    - Since the app requires displaying a constant list of 100 repositories and no continuous data stream, I am not returning a flow of data from repository/usecase.
    - **LiveData** or **MutableStateFlow** could have been used in the view model for observing and updating the UI. I chose **LiveData** for its simplicity and inbuilt lifecycle awareness. **MutableStateFlow** is not lifecycle aware by default and will need `lifecycle-runtime-compose` dependency to use `collectAsStateWithLifecycle` to do the same thing that **LiveData** does.
    - If pagination were implemented, **Flows** would be a better choice, as they provide more efficient handling of data streams.

4. **Gson vs Moshi vs Kotlin Serialization**:
    - I used **Gson** for its simplicity and wide usage, making it a suitable choice given the limited time constraints of the project.
    - Future improvements could include switching to **Moshi** or **Kotlin Serialization**, which offer better Kotlin support and more built-in features, like custom parsing without needing `TypeAdapters`.

# Future Improvements/Enhancements
- **Pagination**: Implement pagination using the **Paging3** library, which offers excellent support for Kotlin Coroutines, Flows, and responsive UI design with retries.
- **Add Room database**: Implement `Room` for local database management. Given the architecture of this app, the implementation would be abstracted away in the data layer where GitHubRepositoryImpl would act as source of truth and have a strategy around fetching data from Room (local database) or the remote API based on the availability / freshness of data.
- **Migrate to using Flows**: Consider switching to using **Flow/MutableStateFlow** if we were to implement **Pagination** or add functionality like Search, so we could leverage Flow capabilities and operators (`Zip`, `Combine` etc.).
- **Migration to Moshi / Kotlin Serialization**: Consider switching to these libraries for better Kotlin support and enhanced parsing capabilities, eliminating the need for `TypeAdapter` handling.