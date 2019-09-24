In your build.gradle file:

dependencies {
  implementation 'com.github.mooaazzzz:writing:1.0.1'
}

In your project.gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

In your Activity class:

// boolean check the touch on the canvas

CanvasView canvasView = new CanvasView(context, boolean , penColor);
        relativeLayout.addView(canvasView);

