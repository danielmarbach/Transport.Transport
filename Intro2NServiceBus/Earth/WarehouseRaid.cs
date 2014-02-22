using System;

namespace Earth
{
    using System.Threading;
    using System.Threading.Tasks;

    using Messages.Events;

    using NServiceBus;

    public class WarehouseRaid : IWantToRunWhenBusStartsAndStops
    {
        private CancellationTokenSource tokenSource = new CancellationTokenSource();

        private Task task;

        public IBus Bus { get; set; }

        public void Start()
        {
            CancellationToken cancellationToken = this.tokenSource.Token;

            this.task = Task.Factory.StartNew(
                () =>
                    {
                        cancellationToken.ThrowIfCancellationRequested();
                        int numberOfOfficers = 1;

                        while (!cancellationToken.IsCancellationRequested)
                        {
                            Guid identification = Guid.NewGuid();

                            // TODO : Create Message PoliceOfficerDied
                            // TODO: Publish message
                            // TODO: Defer message 10 seconds into the future

                            cancellationToken.ThrowIfCancellationRequested();

                            Thread.Sleep(TimeSpan.FromSeconds(5));
                        }
                    },
                cancellationToken);
        }

        public void Stop()
        {
            this.tokenSource.Cancel();

            try
            {
                this.task.Wait();
            }
            catch (AggregateException exception)
            {
                exception.Handle(ex => ex is OperationCanceledException);
            }
        }
    }
}
