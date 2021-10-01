# SpaceX-API
App to demonstrate number of SpaceX flights

## Architecture
- MVVM design pattern using Android Architecture Components (ie LiveData and ViewModel)
- Dagger Hilt for DI
- RxJava for communication between the api/repo & VM layers
- LiveData for communication between VM & UI layers (benefit of this is LiveData is lifecycle aware whereas Rx is not)
- Single activity app using the Navigation component. Not highly useful when it's just one fragment + dialog fragment but would be a good base for adding additional screens to the app

## General comments
- The bulk of the business logic happens in the LaunchListViewModel. In here we combine the CompanyInfo and LaunchesList responses, filter this combined LiveData stream via the use of the filters LiveData and then return this as ViewState LiveData to the fragment.

## Testing
- Added comprehensive tests at the repo layer, essentially just making sure that the responses returned from the API are then correctly mapped to the objects that the app actually uses for business logic. Benefit of having an API response class and its counterpart 'native' class is that we are decoupling the data from the backend from the data we are using on the front end
- Added a lot of tests in the LaunchListViewModel to check what happens in various circumstances of one API call failing when another succeeds etc.

## Ideas for improvement
My time's quite limited so I've tried the best I can in a short time frame. Given more time there's numerous improvements I'd make to the codebase:

- Add UI tests- for example checking that pressing on the filter icon successfully shows the FilterDialogFragment
- Tidy up the DialogFragment logic and behaviour (for instance the yearsTo and yearsFrom text fields do not retain their state across DialogFragment destruction/restoration, code duplication regarding the yearsTo/from textChanged listeners)
- Add caching/persistence. Would do this using Room- the API response can be stored locally.
- Extracting some of the logic out of the DialogFragment into the VM- for example the logic for deducing the date range filter could be extracted
- Tests for the FilterVM logic
- Minor improvements to the LaunchVM - for example I could pass the Schedulers via DI and make sure that the calls are happening on the correct threads. Could also add tests for the filters to make sure they are filtering the response as expected
- Some of the view logic in the adapter/LaunchList fragment could be extracted out
