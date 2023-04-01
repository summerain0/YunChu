package com.cxoip.yunchu.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideIntoContainerLeft() = slideIntoContainer(
    AnimatedContentScope.SlideDirection.Left,
    tween(700)
)

@OptIn(ExperimentalAnimationApi::class)
fun AnimatedContentScope<NavBackStackEntry>.slideIntoContainerRight() = slideOutOfContainer(
    AnimatedContentScope.SlideDirection.Right,
    tween(700)
)