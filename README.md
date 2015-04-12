# FragmentLifecycleCallbacks
An implementation of a callback when a Fragment is added or removed from an Acitvity. The current implementation doesn't account for the fragments visibility, but only if it's been added to or removed from the FragmentManager of the Activity.

This implementation uses a Thread for the active Activity, which polls the FragmentManager of the Activity for the current fragments at a regular interval. If the current fragments are different from the fragments in the last poll, the callback is invoked.