package com.icesoft.magnetlinksearch.mappers;
public class EsCount {
    /**
     * count : 7984077
     * _shards : {"total":1,"successful":1,"skipped":0,"failed":0}
     */

    private int count;
    private ShardsBean _shards;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ShardsBean get_shards() {
        return _shards;
    }

    public void set_shards(ShardsBean _shards) {
        this._shards = _shards;
    }

    public static class ShardsBean {
        /**
         * total : 1
         * successful : 1
         * skipped : 0
         * failed : 0
         */

        private int total;
        private int successful;
        private int skipped;
        private int failed;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSuccessful() {
            return successful;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }

        public int getSkipped() {
            return skipped;
        }

        public void setSkipped(int skipped) {
            this.skipped = skipped;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }
    }
}
