package com.niki.common.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * MVI 架构的 view model 基类
 */
abstract class MVIViewModel<Intent, State, Effect> {
    val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _stateFlow = MutableStateFlow(initUiState())
    val uiStateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    private val _effectFlow = MutableSharedFlow<Effect>()
    val uiEffectFlow: SharedFlow<Effect> by lazy { _effectFlow.asSharedFlow() }

    private val channel = Channel<Intent>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { intent -> handleIntent(intent) }
        }
    }

    /**
     * viewmodel 更新 state
     *
     * view 需要 observeState 才能观察到
     */
    protected fun updateState(update: State.() -> State) =
        _stateFlow.update(update)

    /**
     * view 对 viewmodel 发送数据
     */
    fun sendIntent(intent: Intent) =
        viewModelScope.launch {
            channel.send(intent)
        }

    /**
     * viewmodel 对 view 发送数据
     *
     * view 需要对 effectFlow collect
     */
    protected fun sendEffect(effect: Effect) = viewModelScope.launch { _effectFlow.emit(effect) }

    /**
     * state 初始化
     */
    protected abstract fun initUiState(): State

    /**
     * channel 接收并处理 intent
     */
    protected abstract fun handleIntent(intent: Intent)

    /**
     * 使用 view model scope 来观察
     */
    fun <R> observeState(
        filter: suspend (State) -> R,
        action: suspend (R) -> Unit
    ) {
        observeState(viewModelScope, filter, action)
    }

    /**
     * 状态观察的简化版
     */
    fun <R> observeState(
        scope: CoroutineScope,
        filter: suspend (State) -> R,
        action: suspend (R) -> Unit
    ) {
        uiStateFlow.apply {
            scope.launch {
                map(filter).collect(action)
            }
        }
    }

    suspend fun collectFlow(action: suspend (Effect) -> Unit) {
        uiEffectFlow.collect(action)
    }
}