# Navigation Architecture Guidelines for Kotlin Multiplatform Apps

## Overview

This document outlines a hierarchical navigation architecture for Kotlin Multiplatform applications using Jetpack Navigation with Compose. The architecture consists of a top-level navigation graph and feature-specific navigation graphs that are nested within it.

## Key Components

### Routes

Routes should be defined using sealed interfaces for type safety:

1. **Top-level Routes**:
   ```kotlin
   // Define in a file like Route.kt
   sealed interface Route {
       @Serializable
       data object Home : Route
       
       @Serializable
       data object Auth : Route
       
       @Serializable
       data object FeatureGraph : Route
   }
   ```

2. **Feature-specific Routes**:
   ```kotlin
   // Define in a file like FeatureRoute.kt
   sealed interface FeatureRoute {
       @Serializable
       data object FeatureList : FeatureRoute
       
       @Serializable
       data object FeatureDetail : FeatureRoute
       
       @Serializable
       data object FeatureSettings : FeatureRoute
   }
   ```

Using sealed interfaces for routes provides type safety and enables the use of Kotlin's when expressions for exhaustive handling of navigation destinations.

### Navigation Graphs

1. **Main Navigation Graph**:
   - Define the top-level navigation graph that structures the app
   - Handle global navigation events (e.g., logout)
   - Include nested navigation graphs for features

   ```kotlin
   @Composable
   fun MainNavigationGraph(
       navController: NavHostController = rememberNavController(),
       modifier: Modifier = Modifier,
   ) {
       // Register global navigation handler
       DisposableEffect(Unit) {
           GlobalNavigator.register(
               object : GlobalNavigationHandler {
                   override fun onLogout() {
                       navController.navigate(Route.Auth) {
                           popUpTo(0)
                           launchSingleTop = true
                       }
                   }
                   
                   override fun navigateTo(route: Route) {
                       navController.navigate(route)
                   }
               },
           )
           onDispose {
               GlobalNavigator.unregister()
           }
       }
       
       NavHost(
           navController = navController,
           startDestination = Route.Auth,
           modifier = modifier,
       ) {
           // Auth screen
           composable<Route.Auth> {
               // Observe navigation events
               val viewModel = koinViewModel<AuthViewModel>()
               
               ObserveAsEvents(viewModel.navigationEventsChannelFlow) {
                   when (it) {
                       is AuthViewModel.NavigationEvent.OnAuthSucceeded -> {
                           navController.navigate(Route.Home) {
                               popUpTo(Route.Auth) {
                                   inclusive = true
                               }
                           }
                       }
                   }
               }
               
               AuthScreen(viewModel = viewModel)
           }
           
           // Home screen
           composable<Route.Home> {
               HomeScreen()
           }
           
           // Feature navigation graph
           FeatureNavigationGraph<Route.FeatureGraph>(
               navController = navController,
           )
       }
   }
   ```

2. **Feature Navigation Graphs**:
   - Nested within the main navigation graph
   - Define navigation within a specific feature
   - Use shared ViewModels to pass data between screens

   ```kotlin
   @OptIn(KoinExperimentalAPI::class)
   internal inline fun <reified T : Route> NavGraphBuilder.FeatureNavigationGraph(
       navController: NavController,
       startDestination: FeatureRoute = FeatureRoute.FeatureList,
   ) {
       navigation<T>(
           startDestination = startDestination,
       ) {
           composable<FeatureRoute.FeatureList> { entry ->
               val viewModel = entry.sharedKoinViewModel<FeatureListViewModel>(navController)
               
               ObserveAsEvents(viewModel.navigationEventsChannelFlow) {
                   when (it) {
                       is FeatureNavigator.NavigationEvents.NavigateToDetail -> {
                           navController.navigate(FeatureRoute.FeatureDetail)
                       }
                   }
               }
               
               FeatureListScreen(viewModel = viewModel)
           }
           
           composable<FeatureRoute.FeatureDetail> { entry ->
               val viewModel = entry.sharedKoinViewModel<FeatureListViewModel>(navController)
               
               FeatureDetailScreen(viewModel = viewModel)
           }
       }
   }
   ```

### Navigation Channels

Use Kotlin Channels to handle navigation events in a unidirectional data flow pattern:

1. **Define navigation events in ViewModels**:
   ```kotlin
   sealed interface NavigationEvent {
       data object NavigateToHome : NavigationEvent
       data object NavigateToSettings : NavigationEvent
       data class NavigateToDetail(val itemId: String) : NavigationEvent
   }
   
   private val navigationChannel = Channel<NavigationEvent>()
   val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()
   ```

2. **Emit navigation events from ViewModels**:
   ```kotlin
   fun onItemClicked(itemId: String) {
       viewModelScope.launch {
           navigationChannel.send(NavigationEvent.NavigateToDetail(itemId))
       }
   }
   ```

3. **Observe navigation events in the UI**:
   ```kotlin
   @Composable
   fun FeatureScreen(viewModel: FeatureViewModel) {
       ObserveAsEvents(viewModel.navigationEventsChannelFlow) {
           when (it) {
               is NavigationEvent.NavigateToDetail -> {
                   navController.navigate(FeatureRoute.Detail.createRoute(it.itemId))
               }
               NavigationEvent.NavigateToHome -> {
                   navController.navigate(Route.Home)
               }
               NavigationEvent.NavigateToSettings -> {
                   navController.navigate(FeatureRoute.Settings)
               }
           }
       }
       
       // Screen content
   }
   ```

This approach decouples the UI from navigation logic, making the code more testable and maintainable.

## Global Navigation

Implement a global navigation mechanism through a singleton:

1. **GlobalNavigator**:
   ```kotlin
   object GlobalNavigator {
       private var handler: GlobalNavigationHandler? = null
       
       fun register(handler: GlobalNavigationHandler) {
           this.handler = handler
       }
       
       fun unregister() {
           this.handler = null
       }
       
       fun logout() {
           handler?.onLogout()
       }
       
       fun navigateTo(route: Route) {
           handler?.navigateTo(route)
       }
   }
   
   interface GlobalNavigationHandler {
       fun onLogout()
       fun navigateTo(route: Route)
   }
   ```

2. **Usage**:
   ```kotlin
   // From anywhere in the app
   GlobalNavigator.logout()
   
   // Or navigate to a specific route
   GlobalNavigator.navigateTo(Route.Settings)
   ```

## Utility Functions

Implement utility functions to simplify navigation:

1. **ObserveAsEvents**:
   ```kotlin
   @Composable
   fun <T> ObserveAsEvents(
       flow: Flow<T>,
       onEvent: (T) -> Unit,
   ) {
       val lifecycleOwner = LocalLifecycleOwner.current
       LaunchedEffect(flow, lifecycleOwner.lifecycle) {
           lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
               withContext(Dispatchers.Main.immediate) {
                   flow.collect(onEvent)
               }
           }
       }
   }
   ```

2. **sharedKoinViewModel**:
   ```kotlin
   @Composable
   inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(navController: NavController): T {
       val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
       val parentEntry = remember(this) {
           navController.getBackStackEntry(navGraphRoute)
       }
       return koinViewModel(
           viewModelStoreOwner = parentEntry,
       )
   }
   ```

## Navigation Flow Examples

### Authentication to Home Flow

1. App starts at the Auth screen (`Route.Auth`)
2. User authenticates successfully
3. AuthViewModel emits `NavigationEvent.OnAuthSucceeded`
4. MainNavigationGraph observes the event and navigates to `Route.Home`

### Feature Navigation Flow

1. User navigates to a feature from the home screen
2. User selects an item from a list
3. FeatureListViewModel emits `NavigationEvent.NavigateToDetail`
4. FeatureNavigationGraph observes the event and navigates to the detail screen
5. User can navigate back to the list or to other screens within the feature

## Implementation Steps

1. **Define Routes**:
   - Create sealed interfaces for top-level and feature-specific routes
   - Use the `@Serializable` annotation for each route

2. **Create ViewModels with Navigation Events**:
   - Define navigation events as sealed interfaces
   - Create a channel for emitting navigation events
   - Expose the channel as a flow

3. **Implement Navigation Graphs**:
   - Create a main navigation graph
   - Create feature-specific navigation graphs
   - Use the `navigation` extension function for nested graphs

4. **Set Up Global Navigation**:
   - Implement the GlobalNavigator singleton
   - Register a handler in the main navigation graph

5. **Create Utility Functions**:
   - Implement ObserveAsEvents for handling navigation events
   - Implement sharedKoinViewModel for sharing ViewModels across screens

## Best Practices

1. **Keep Navigation Logic in ViewModels**:
   - ViewModels should decide when to navigate
   - UI should only observe and execute navigation events

2. **Use Type-Safe Routes**:
   - Define routes as sealed interfaces
   - Use generics with composable functions

3. **Share ViewModels When Needed**:
   - Use sharedKoinViewModel for screens that need to share data
   - Consider using SavedStateHandle for persisting data across process death

4. **Handle Back Navigation Properly**:
   - Use popUpTo and inclusive parameters to control the back stack
   - Consider using custom back handlers for complex flows

5. **Test Navigation Logic**:
   - Write unit tests for ViewModel navigation events
   - Use test doubles for navigation handlers

## Conclusion

This navigation architecture provides a flexible and type-safe way to handle navigation in Kotlin Multiplatform apps. By using Kotlin Channels for navigation events and utility functions to simplify common tasks, the architecture maintains a clean separation of concerns between UI, business logic, and navigation.