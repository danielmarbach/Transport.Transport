namespace Messages.Events
{
    using System;

    public interface PoliceOfficerDied
    {
        Guid Identification { get; set; }

        string Name { get; set; }
    }
}
