## 布隆过滤器
在大数据量中，判断一条数据是否在这堆大量数据中；

优点：由于存放的不是完整的数据，所以占用的内存很少，而且新增，查询速度够快；

缺点：随着数据的增加，误判率随之增加；无法做到删除数据；
只能判断数据是否一定不存在，而无法判断数据是否一定存在。(理解：过滤结果如果判断数据不存在那么这条数据实际一定不存在，而过滤结果判断数据存在就不一定实际存在了)