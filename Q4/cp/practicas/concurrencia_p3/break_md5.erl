-module(break_md5).
-define(PASS_LEN, 6).
-define(UPDATE_BAR_GAP, 1000).
-define(BAR_SIZE, 40).
-define(NUM_THREADS, 4).

-export([break_md5s/1, break_md5/1, pass_to_num/1, num_to_pass/1]).
-export([progress_loop/2]).
-export([break_md5_thread/5]).

% Base ^ Exp

pow_aux(_Base, Pow, 0) -> Pow;
pow_aux(Base, Pow, Exp) when Exp rem 2 == 0 ->
    pow_aux(Base*Base, Pow, Exp div 2);
pow_aux(Base, Pow, Exp) -> pow_aux(Base, Base * Pow, Exp - 1).

pow(Base, Exp) -> pow_aux(Base, 1, Exp).

%% Number to password and back conversion

num_to_pass_aux(_N, 0, Pass) -> Pass;
num_to_pass_aux(N, Digit, Pass) ->
    num_to_pass_aux(N div 26, Digit - 1, [$a + N rem 26 | Pass]).

num_to_pass(N) -> num_to_pass_aux(N, ?PASS_LEN, []).

pass_to_num_aux([], Num) -> Num;
pass_to_num_aux([C|T], Num) -> pass_to_num_aux(T, Num*26 + C-$a).

pass_to_num(Pass) -> pass_to_num_aux(Pass, 0).

%% Hex string to Number

hex_char_to_int(N) ->
    if (N >= $0) and (N =< $9) -> N-$0;
       (N >= $a) and (N =< $f) -> N-$a+10;
       (N >= $A) and (N =< $F) -> N-$A+10;
       true -> throw({not_hex, [N]})
    end.

hex_string_to_num_aux([], Num) -> Num;
hex_string_to_num_aux([Hex|T], Num) ->
    hex_string_to_num_aux(T, Num*16 + hex_char_to_int(Hex)).

hex_string_to_num(Hex) -> hex_string_to_num_aux(Hex, 0).

%% Progress bar runs in its own process

progress_loop(N, Bound) ->
    receive
        stop -> ok;
        {progress_report, Checked} ->
            N2 = N + Checked,
            Full_N = N2 * ?BAR_SIZE div Bound,
            Full = lists:duplicate(Full_N, $=),
            Empty = lists:duplicate(?BAR_SIZE - Full_N, $-),
            io:format("\r[~s~s] ~.2f%", [Full, Empty, N2/Bound*100]),
            progress_loop(N2, Bound)
    end.

break_md5_thread(_, N, N, Master_Pid, _) -> 
    Master_Pid ! {finished, self()},
    ok;
break_md5_thread(Hashes, N, Bound, Master_Pid, Progress_Pid) ->
    if 
        N rem ?UPDATE_BAR_GAP == 0 ->
            Progress_Pid ! {progress_report, ?UPDATE_BAR_GAP};
        true ->
            ok
    end,
    receive
        stop -> ok
    after 0 ->
        Pass = num_to_pass(N),
        Hash = crypto:hash(md5, Pass),
        Num_Hash = binary:decode_unsigned(Hash),
        case lists:member(Num_Hash, Hashes) of
            true ->
                io:format("\e[2K\r~.16B: ~s~n", [Num_Hash, Pass]),
                Master_Pid ! {founded, Num_Hash},
                break_md5_thread(lists:delete(Num_Hash, Hashes), N, Bound, Master_Pid, Progress_Pid);
            false ->
                break_md5_thread(lists:delete(Num_Hash, Hashes), N+1, Bound, Master_Pid, Progress_Pid)
        end
    end.


%% Exit threads
exit_threads([]) ->
    ok;
exit_threads([Pid | T]) ->
    Pid ! stop,
    exit_threads(T).

%% Init threads
init_threads(?NUM_THREADS, _, _, Thread_Pids, _) -> 
    Thread_Pids;
init_threads(N, Hashes, Bound, Thread_Pids, Progress_Pid) ->
    Pid_Thread = spawn(?MODULE, break_md5_thread, [Hashes, N*Bound div ?NUM_THREADS, (N+1)*Bound div ?NUM_THREADS, self(), Progress_Pid]),
    init_threads(N+1, Hashes, Bound, Thread_Pids++[Pid_Thread], Progress_Pid).


%% break_md5/2 iterates checking the possible passwords
start_master(Hashes, Bound, Progress_Pid) ->
    break_md5(Hashes, init_threads(0, Hashes, Bound, [], Progress_Pid)).

break_md5([], Thread_Pids) ->
    exit_threads(Thread_Pids),
    ok; % No more hashes to find
break_md5(Hashes, []) ->
    {not_found, Hashes};  % Checked every possible password
break_md5(Hashes, Thread_Pids) ->
    receive
        {founded, Num_Hash} ->
            break_md5(lists:delete(Num_Hash, Hashes), Thread_Pids);
        {finished, Pid} ->
            break_md5(Hashes, lists:delete(Pid, Thread_Pids))
    end.

%% Break a list of hashes

break_md5s(Hashes) ->
    Bound = pow(26, ?PASS_LEN),
    Progress_Pid = spawn(?MODULE, progress_loop, [0, Bound]),
    Num_Hashes = lists:map(fun hex_string_to_num/1, Hashes),
    Res = start_master(Num_Hashes, Bound, Progress_Pid),
    Progress_Pid ! stop,
    Res.

%% Break a single hash

break_md5(Hash) -> break_md5s([Hash]).
