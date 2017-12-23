#author mazhuang
****用法（支持组合动画）******
ArrayList<Object> animators = new ArrayList<>();
animators.add(ObjectAnimatorProxy.ofFloat(view1, "alpha", 0.0f, 1.0f));
animators.add(ObjectAnimatorProxy.ofFloat(view2, "alpha", 0.0f, 1.0f));

AnimatorSetProxy animatorSetProxy = new AnimatorSetProxy();
animatorSetProxy.playTogether(animators);
animatorSetProxy.setDuration(200);
animatorSetProxy.addListener(new AnimatorListenerAdapterProxy() {})
animatorSetProxy.start();


*****支持的属性如下******
PROXY_PROPERTIES.put("alpha", ALPHA);
PROXY_PROPERTIES.put("pivotX", PIVOT_X);
PROXY_PROPERTIES.put("pivotY", PIVOT_Y);
PROXY_PROPERTIES.put("translationX", TRANSLATION_X);
PROXY_PROPERTIES.put("translationY", TRANSLATION_Y);
PROXY_PROPERTIES.put("rotation", ROTATION);
PROXY_PROPERTIES.put("rotationX", ROTATION_X);
PROXY_PROPERTIES.put("rotationY", ROTATION_Y);
PROXY_PROPERTIES.put("scaleX", SCALE_X);
PROXY_PROPERTIES.put("scaleY", SCALE_Y);
PROXY_PROPERTIES.put("scrollX", SCROLL_X);
PROXY_PROPERTIES.put("scrollY", SCROLL_Y);
PROXY_PROPERTIES.put("x", X);
PROXY_PROPERTIES.put("y", Y);