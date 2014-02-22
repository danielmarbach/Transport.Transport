namespace Heaven
{
    using System;
    using System.Collections.Concurrent;

    public static class Departments
    {
        private static readonly Random random = new Random();

        private static readonly ConcurrentDictionary<Guid, PoliceOfficer> officers = new ConcurrentDictionary<Guid, PoliceOfficer>(); 

        public static void RegisterOfficer(PoliceOfficer officer)
        {
            Console.WriteLine("Department: Registering arrived officer {0}.", officer.Name);

            officers.AddOrUpdate(officer.Identification, k => officer, (k, o) => officer);
        }

        public static PoliceOfficer SelectForRecruitment(Guid identification)
        {
            PoliceOfficer officer;
            officers.TryGetValue(identification, out officer);
            return officer;
        }

        public static void RecruitByMildredProctor(PoliceOfficer officer)
        {
            Console.WriteLine("Department: Check officer {0} by Mildred Proctor.", officer.Name);
            if (random.Next(2) == 0)
            {
                Console.WriteLine("Department: Mildred Proctor is recruiting {0}.", officer.Name);

                officer.Recruit();

                Console.WriteLine("Department: Mildred Proctor has recruited {0}.", officer.Name);

                officers.AddOrUpdate(officer.Identification, k => officer, (k, o) => officer);
            }
        }

        public static PoliceOfficer HuntDeados(Guid identification)
        {
            PoliceOfficer officer;
            officers.TryGetValue(identification, out officer);
            return officer;
        }
    }
}